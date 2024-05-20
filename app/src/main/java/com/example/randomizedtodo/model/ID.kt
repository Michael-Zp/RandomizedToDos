package com.example.randomizedtodo.model

import java.util.UUID

class ID {
    private val id: String;

    constructor() {
        id = UUID.randomUUID().toString()
    }

    constructor(id: UUID) {
        this.id = id.toString()
    }

    constructor(id: String) {
        this.id = UUID.fromString(id).toString()
    }

    override fun toString(): String {
        return id
    }

    override fun equals(other: Any?): Boolean {
        if (other is ID)
        {
            return this.id == other.id
        }

        return false
    }

    override fun hashCode(): Int {
        return id.hashCode()
    }
}