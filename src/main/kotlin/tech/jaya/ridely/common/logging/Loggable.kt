package tech.jaya.ridely.common.logging

import org.slf4j.LoggerFactory
import org.slf4j.Logger

abstract class Loggable {
    val log: Logger = LoggerFactory.getLogger(this::class.java)
}