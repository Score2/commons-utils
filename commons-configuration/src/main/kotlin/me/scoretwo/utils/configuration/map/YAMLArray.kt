package me.scoretwo.utils.configuration.map

import com.alibaba.fastjson.JSONArray

open class YAMLArray: JSONArray {

    constructor(jsonArray: JSONArray): super(jsonArray)

    constructor(): super()


}