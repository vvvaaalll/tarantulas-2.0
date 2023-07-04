package hr.vloboda.tarantulas.model

import java.time.LocalDate

class User(
    var id: Long = 0,
    var username: String = "",
    var email: String = "",
    var firstName: String = "",
    var lastName: String = "",
    var isActive: Boolean = false,
    var avatarUrl: String = "",
    var dateOfSignUp: String = "",
    var roles: Set<Role> = emptySet()
) {}


data class Role(
    val id: Long = 0,
    val name: String? = null,
    val description: String? = null,
) {}
