package com.example.pokemon.Repository

import com.example.pokemon.datastore.DataStoreManager
import com.example.pokemon.local.User
import com.example.pokemon.local.UserDao
import io.mockk.every
import io.mockk.mockk
import io.mockk.verify
import kotlinx.coroutines.flow.Flow
import kotlinx.coroutines.runBlocking
import org.junit.Assert.*

import org.junit.Before
import org.junit.Test

class UserRepositoryTest {

    private lateinit var userDao: UserDao
    private lateinit var dataStoreManager: DataStoreManager


    private lateinit var repository: UserRepository


    @Before
    fun setUp() {
        userDao = mockk()
        dataStoreManager = mockk()
        repository = UserRepository(userDao,dataStoreManager)
    }

    @Test
    fun login():Unit= runBlocking {
        val loginResponse = mockk<User>()
        every {
            runBlocking {
                userDao.login("aa","aa")
            }
        }returns loginResponse

        repository.login("aa","aa")

        verify {
            runBlocking {
                userDao.login("aa","aa")
            }
        }
    }


    @Test
    fun register():Unit= runBlocking {
        val registerResponse = mockk<Long>()
        val register = User(null,"zaa","zaa","zaa",null)
        every {
            runBlocking {
                userDao.insertUser(register)
            }
        }returns registerResponse

        repository.register(register)

        verify {
            runBlocking {
                userDao.insertUser(register)
            }
        }
    }

    @Test
    fun updateUser():Unit= runBlocking {
        val updateResponse = mockk<Int>()
        val update = User(10,"zaa","zzz","zzz","")
        every {
            runBlocking {
                userDao.updateUser(update)
            }
        }returns updateResponse

        repository.updateUser(update)

        verify {
            runBlocking {
                userDao.updateUser(update)
            }
        }
    }

    @Test
    fun setUser():Unit= runBlocking {
        val setUserResponse = mockk<Unit>()
        val set = User(10,"zzz","zzz","zzz","")
        every {
            runBlocking {
                dataStoreManager.setUser(set)
            }
        }returns setUserResponse

        repository.setUser(set)

        verify {
            runBlocking {
                dataStoreManager.setUser(set)
            }
        }
    }

    @Test
    fun getUserPref():Unit= runBlocking {
        val getUserResponse = mockk<Flow<User>>()
        every {
            runBlocking {
                dataStoreManager.getUser()
            }
        }returns getUserResponse

        repository.getUserPref()

        verify {
            runBlocking {
                dataStoreManager.getUser()
            }
        }
    }
}