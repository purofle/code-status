package tech.archlinux.codestatus.repository

import org.springframework.data.repository.kotlin.CoroutineCrudRepository
import org.springframework.stereotype.Repository
import tech.archlinux.codestatus.entity.AccountEntity

@Repository
interface AccountRepository : CoroutineCrudRepository<AccountEntity, Long> {
    fun existsAccountEntityByNodeId(nodeId: String): Boolean
    fun findAccountEntityByLogin(login: String): AccountEntity?
    fun findAccountEntityByNodeId(nodeId: String): AccountEntity?
}
