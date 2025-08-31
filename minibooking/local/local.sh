#!/usr/bin/env bash

API_BASE="${API_BASE:-http://localhost:8081}"
PAYLOAD_FILE="${1:-local/createReservation/booking.json}"
POLL_SECONDS="${POLL_SECONDS:-30}"

need() {
  if ! command -v "$1" >/dev/null 2>&1; then
    echo "❌ Missing dependency: $1"
    echo "   - macOS: brew install $1"
    exit 1
  fi
}

need curl
need jq

if [[ ! -f "$PAYLOAD_FILE" ]]; then
  echo "❌ Payload file not found: $PAYLOAD_FILE"
  exit 1
fi

echo "➡️  POST $API_BASE/bookings with $PAYLOAD_FILE"
CREATE_RESP="$(curl -sS -X POST "$API_BASE/bookings" \
  -H 'Content-Type: application/json' \
  --data "@${PAYLOAD_FILE}")"

echo "↩︎  Response: $CREATE_RESP"
BOOKING_ID="$(echo "$CREATE_RESP" | jq -r '.bookingId // empty')"

if [[ -z "$BOOKING_ID" || "$BOOKING_ID" == "null" ]]; then
  echo "❌ bookingId not found in response"
  exit 1
fi

echo "✅ bookingId: $BOOKING_ID"
echo "🔎 Polling status for up to ${POLL_SECONDS}s..."

END=$((SECONDS + POLL_SECONDS))
LAST_JSON=""

while (( SECONDS < END )); do
  # Prova a leggere il documento
  if ! LAST_JSON="$(curl -sS "$API_BASE/bookings/$BOOKING_ID")"; then
    sleep 1
    continue
  fi

  # Se non esiste ancora (404), riprova
  if [[ -z "$LAST_JSON" || "$LAST_JSON" == "Not Found" ]]; then
    sleep 1
    continue
  fi

  STATUS="$(echo "$LAST_JSON" | jq -r '.status // empty' 2>/dev/null || echo "")"

  # Se status presente e non è più REQUESTED, abbiamo finito
  if [[ -n "$STATUS" && "$STATUS" != "REQUESTED" ]]; then
    echo "✅ Final status: $STATUS"
    echo "$LAST_JSON" | jq .
    exit 0
  fi

  sleep 1
done

echo "⌛ Timeout waiting for final status (>${POLL_SECONDS}s). Latest document:"
{ echo "$LAST_JSON" | jq .; } || echo "$LAST_JSON"
exit 1
