rootProject.name = "minibooking"
include(
    "libs:common-domain",
    "libs:common-kafka",
    "services:booking-service",
    "services:resource-service",
    "services:orchestrator-service"
)
