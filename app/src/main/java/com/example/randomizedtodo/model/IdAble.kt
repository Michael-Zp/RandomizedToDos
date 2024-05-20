package com.example.randomizedtodo.model

import java.util.UUID

open class IdAble {
    val ID: ID;

    constructor() {
        ID = ID()
    }

    constructor(id: UUID) {
        ID = ID(id)
    }

    constructor(id: String) {
        ID = ID(id)
    }
}