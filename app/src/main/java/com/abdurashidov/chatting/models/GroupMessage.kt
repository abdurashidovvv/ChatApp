package com.abdurashidov.chatting.modelsclass GroupMessage {    var author:String?=null    var image:String?=null    var message:String?=null    var date:String?=null    var uid:String?=null    constructor(author: String?, image: String?, message: String?, date: String?, uid: String?) {        this.author = author        this.image = image        this.message = message        this.date = date        this.uid = uid    }    constructor()    override fun toString(): String {        return "GroupMessage(author=$author, image=$image, message=$message, date=$date, uid=$uid)"    }}