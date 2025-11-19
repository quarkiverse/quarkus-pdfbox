<div align="center">

<img src="https://github.com/quarkiverse/.github/blob/main/assets/images/quarkus.svg" width="67" height="70" ><img src="https://github.com/quarkiverse/.github/blob/main/assets/images/plus-sign.svg" height="70" ><img src="https://svn.apache.org/repos/asf/comdev/project-logos/originals/pdfbox.svg" height="70" >

# Quarkus PDFBox
</div>
<br>

<!-- ALL-CONTRIBUTORS-BADGE:START - Do not remove or modify this section -->
[![All Contributors](https://img.shields.io/badge/all_contributors-2-orange.svg?style=flat-square)](#contributors-)
<!-- ALL-CONTRIBUTORS-BADGE:END -->

[![Version](https://img.shields.io/maven-central/v/io.quarkiverse.pdfbox/quarkus-pdfbox?logo=apache-maven&style=for-the-badge)](https://search.maven.org/artifact/io.quarkiverse.pdfbox/quarkus-pdfbox)
[![License](https://img.shields.io/github/license/quarkiverse/quarkus-pdfbox?style=for-the-badge)]()

This extension provides Apache PDFBox integration with Quarkus. 

## Usage

Add the dependency to your project:

```xml
<dependency>
    <groupId>io.quarkiverse.pdfbox</groupId>
    <artifactId>quarkus-pdfbox</artifactId>
    <version>LATEST</version>
</dependency>
```
## Documentation

Documentation is published in https://docs.quarkiverse.io/quarkus-pdfbox/dev/index.html 

## Docker

When building native images in Docker using the standard Quarkus Docker configuration files some additional features need to be
installed to support Apache POI.  Specifically font information is not included in [Red Hat's ubi-minimal images](https://developers.redhat.com/products/rhel/ubi).  To install it
simply add these lines to your `DockerFile.native` file:

```shell
FROM registry.access.redhat.com/ubi9/ubi-minimal:9.5

######################### Set up environment for POI #############################
RUN microdnf update -y && microdnf install -y freetype fontconfig && microdnf clean all
######################### Set up environment for POI #############################

WORKDIR /work/
RUN chown 1001 /work \
    && chmod "g+rwX" /work \
    && chown 1001:root /work
# Shared objects to be dynamically loaded at runtime as needed,
COPY --chown=1001:root --chmod=0755 target/*.properties target/*.so /work/
COPY --chown=1001:root --chmod=0755 target/*-runner /work/application
# Permissions fix for Windows
RUN chmod "ugo+x" /work/application
EXPOSE 8080
USER 1001

CMD ["./application", "-Dquarkus.http.host=0.0.0.0"]
```

> [!CAUTION]
> Make sure `.dockerignore` does not exclude `.so` files!

## Contributors ✨

Thanks goes to these wonderful people ([emoji key](https://allcontributors.org/docs/en/emoji-key)):

<!-- ALL-CONTRIBUTORS-LIST:START - Do not remove or modify this section -->
<!-- prettier-ignore-start -->
<!-- markdownlint-disable -->
<table>
  <tbody>
    <tr>
      <td align="center" valign="top" width="14.28%"><a href="http://gastaldi.wordpress.com"><img src="https://avatars.githubusercontent.com/u/54133?v=4?s=100" width="100px;" alt="George Gastaldi"/><br /><sub><b>George Gastaldi</b></sub></a><br /><a href="https://github.com/quarkiverse/quarkus-pdfbox/commits?author=gastaldi" title="Code">💻</a> <a href="#maintenance-gastaldi" title="Maintenance">🚧</a></td>
      <td align="center" valign="top" width="14.28%"><a href="https://melloware.com"><img src="https://avatars.githubusercontent.com/u/4399574?v=4?s=100" width="100px;" alt="Melloware"/><br /><sub><b>Melloware</b></sub></a><br /><a href="https://github.com/quarkiverse/quarkus-pdfbox/commits?author=melloware" title="Code">💻</a> <a href="#maintenance-melloware" title="Maintenance">🚧</a></td>
    </tr>
  </tbody>
</table>

<!-- markdownlint-restore -->
<!-- prettier-ignore-end -->

<!-- ALL-CONTRIBUTORS-LIST:END -->

This project follows the [all-contributors](https://github.com/all-contributors/all-contributors) specification. Contributions of any kind welcome!
