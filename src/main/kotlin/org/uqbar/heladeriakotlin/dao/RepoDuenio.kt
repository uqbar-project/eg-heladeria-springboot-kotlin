package org.uqbar.heladeriakotlin.dao

import org.springframework.data.repository.CrudRepository
import org.springframework.stereotype.Repository
import org.uqbar.heladeriakotlin.model.Duenio

@Repository
interface RepoDuenio : CrudRepository<Duenio, Long>