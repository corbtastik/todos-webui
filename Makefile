# =============================================================================
# Makefile variables
# =============================================================================
IMAGE_NAME=todos-webui
IMAGE_VERSION=1.0.5
IMAGE_TAG=v$(IMAGE_VERSION)
IMAGE_REPO=quay.io/corbsmartin/$(IMAGE_NAME)

image:
	@podman build --no-cache -t $(IMAGE_NAME):$(IMAGE_TAG) --build-arg=APP_VERSION=$(IMAGE_VERSION) .

push: image
	@podman tag $(IMAGE_NAME):$(IMAGE_TAG) $(IMAGE_REPO):$(IMAGE_TAG)
	@podman push $(IMAGE_REPO):$(IMAGE_TAG)

run:
	@podman run --name $(IMAGE_NAME) -d -p 8080:8080 $(IMAGE_NAME):$(IMAGE_TAG)

build:
	@mvn versions:set -DnewVersion=$(IMAGE_VERSION)
	@mvn clean package -DskipTests=true