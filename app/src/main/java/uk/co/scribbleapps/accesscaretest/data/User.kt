package uk.co.scribbleapps.accesscaretest.data

data class User(
	val username: String,
	val password: String,
	val firstName: String,
	val surname: String,
	val emailAddress: String,
	val yearOfBirth: String,
	val photoURI: String
)