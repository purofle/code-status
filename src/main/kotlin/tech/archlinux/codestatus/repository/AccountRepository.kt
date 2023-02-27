package tech.archlinux.codestatus.repository

import org.springframework.data.repository.CrudRepository
import org.springframework.stereotype.Repository
import tech.archlinux.codestatus.entity.Account

@Repository
interface AccountRepository : CrudRepository<Account, Int> {
    fun existsAccountById(id: Int): Boolean
}
