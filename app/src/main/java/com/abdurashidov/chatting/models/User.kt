package com.abdurashidov.chatting.modelsclass User{    var email:String?=null    var displayName:String?=null    var phoneNumber:String?=null    var photoUrl:String?=null    var uid:String?=null    constructor(        email: String?,        displayName: String?,        phoneNumber: String?,        photoUrl: String?,        uid: String?    ) {        this.email = email        this.displayName = displayName        this.phoneNumber = phoneNumber        this.photoUrl = photoUrl        this.uid = uid    }    constructor()    override fun toString(): String {        return "User(email=$email, displayName=$displayName, phoneNumber=$phoneNumber, photoUrl=$photoUrl, uid=$uid)"    }}