package tech.archlinux.codestatus.repository

import org.springframework.data.repository.CrudRepository
import org.springframework.stereotype.Repository
import tech.archlinux.codestatus.entity.AccountEntity

@Repository
interface AccountRepository : CrudRepository<AccountEntity, Int> {
    fun existsAccountById(id: Int): Boolean
    fun findAccountEntityById(id: Int): AccountEntity?
}
