package me.scoretwo.utils.exceptions

class CommandException(valueName: String, cause: String): Exception("$valueName cannot be $cause!")