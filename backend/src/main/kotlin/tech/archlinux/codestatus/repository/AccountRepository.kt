package tech.archlinux.codestatus.repository

import org.springframework.data.jpa.repository.JpaRepository
import org.springframework.stereotype.Repository
import tech.archlinux.codestatus.entity.AccountEntity

@Repository
interface AccountRepository : JpaRepository<AccountEntity, String> {
    fun existsAccountById(id: String): Boolean
    fun findAccountEntityById(id: String): AccountEntity?
}
