
# Ejemplo Heladería

[![build](https://github.com/uqbar-project/eg-heladeria-springboot-kotlin/actions/workflows/build.yml/badge.svg)](https://github.com/uqbar-project/eg-heladeria-springboot-kotlin/actions/workflows/build.yml) [![codecov](https://codecov.io/gh/uqbar-project/eg-heladeria-springboot-kotlin/branch/main/graph/badge.svg?token=xQ8TFzLbHV)](https://codecov.io/gh/uqbar-project/eg-heladeria-springboot-kotlin)

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

## Módulo de Seguridad

Esta aplicación utiliza Spring Security, de manera que cuando levanta la aplicación crea dos usuarios

- `dodain`, con el rol `admin` (password: `1234`)
- `phm`, con el rol `readonly` (password: `phm`)

Para usar la aplicación desde un cliente podés usar

- [Bruno (recomendado)](./Heladeria_Bruno.json)
- o [Postman](./Heladeria_Postman.json)

Primero tenés que loguearte con algún usuario y luego podés 

- ver la información de las heladerías, o de una heladería, o ver las personas que son dueñas de heladerías
- y si sos admin, podés agregar nuevos dueños o bien actualizar la información de una heladería

Para eso el login chequea las credenciales y devuelve luego un JWT (JSON Web Token) que nosotros almacenamos como variable para luego enviar en cada request.

Para más información podés ver [este apunte](https://docs.google.com/presentation/d/1fZw_wVdpESRp7xPIEy9jfBTOtypQLSvvahIldZ1cVBo/edit?usp=sharing).
