package com.example.randomizedtodo.model

abstract class VersionUpdater<TOld, TNew> {
    abstract fun convert(old: TOld) : TNew;
}