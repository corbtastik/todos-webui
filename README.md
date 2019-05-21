# Todos WebUI

Vue.js, HTML, CSS wrapped as a Spring Boot app.
 
* [Spring Boot](https://spring.io/projects/spring-boot) for app bits
* [Vue.js](https://vuejs.org/) for frontend, inspired by [TodoMVC Vue App](http://todomvc.com/examples/vue/), difference is this one is vendored as a Spring Boot app and calls a backing endpoint (``/todos``)

This application assumes the ``/todos`` endpoint is exposed from the same "origin".  Because of this its best to use this application behind the Todos-Edge which will serve as a gateway and single origin to the client for both loading ``todos-webui`` and for proxying API calls to ``/todos``.

## Steps to run

1. fork this repo and clone
1. mvnw clean package
1. modify ``manifest.yml``
1. login to pcf and cf push

Running this app without an edge service is fine, however the data will just be saved on the browser.  To have data sent to the backend run this app with [todos-edge]() and access through the edge's published endpoint (See todos-edge, README).

**WebUI running**

<p align="center">
    <img src="https://github.com/corbtastik/todos-images/raw/master/todos-webui/ui.png" width="640">
</p>


