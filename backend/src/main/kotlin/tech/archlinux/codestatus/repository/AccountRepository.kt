package tech.archlinux.codestatus.repository

import org.springframework.data.jpa.repository.JpaRepository
import org.springframework.stereotype.Repository
import tech.archlinux.codestatus.entity.AccountEntity

@Repository
interface AccountRepository : JpaRepository<AccountEntity, Int> {
    fun existsAccountById(id: Int): Boolean
    fun findAccountEntityById(id: Int): AccountEntity?
}
