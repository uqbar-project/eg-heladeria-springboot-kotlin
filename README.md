
# Ejemplo Heladería

[![build](https://github.com/uqbar-project/eg-heladeria-springboot-kotlin/actions/workflows/build.yml/badge.svg)](https://github.com/uqbar-project/eg-heladeria-springboot-kotlin/actions/workflows/build.yml) [![codecov](https://codecov.io/gh/uqbar-project/eg-heladeria-springboot-kotlin/branch/master/graph/badge.svg?token=xQ8TFzLbHV)](https://codecov.io/gh/uqbar-project/eg-heladeria-springboot-kotlin)

Este ejemplo está implementado con Kotlin y Spring Boot, se recomienda acompañarlo con el frontend desarrollado en [Svelte](https://github.com/uqbar-project/eg-heladeria-svelte).

Además necesitarás tener levantada una base de datos MySQL con un esquema llamado `heladeria`

```sql
drop schema if exists heladeria;
create schema if not exists heladeria;
```

Encontrarás:

- un modelo de datos compuesto por heladerías, que definen un mapa de gustos (strings) con dificultades de fabricación (integers)
- las heladerías pueden ser artesanales, económicas o industriales, implementadas con un enum
- hay además una relación many-to-one con su dueño

