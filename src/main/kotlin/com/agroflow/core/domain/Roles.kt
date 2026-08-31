package com.agroflow.core.domain

import java.util.UUID

object Roles {
    val ADMIN: UUID = UUID.fromString("ef5ee967-eb0c-491e-b77f-663dfc88510b")
    val AGRICULTOR: UUID = UUID.fromString("6fe0a91a-1318-499e-bedf-6722914d61fd")
    val TRABAJADOR: UUID = UUID.fromString("7896dd16-8aa2-4161-ba08-afe874300fe5")
    val CLIENTE: UUID = UUID.fromString("a42f1efb-78bd-45e4-8a95-7b1c863663bf")
}
