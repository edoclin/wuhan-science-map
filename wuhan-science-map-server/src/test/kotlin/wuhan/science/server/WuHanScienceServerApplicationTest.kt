package wuhan.science.server

import cn.hutool.db.nosql.redis.RedisDS
import org.junit.jupiter.api.Test
import org.springframework.boot.test.context.SpringBootTest
import org.springframework.test.context.ActiveProfiles
import redis.clients.jedis.Jedis
import wuhan.science.server.service.IZhihuiTripItemService
import javax.annotation.Resource

@ActiveProfiles("dev")
@SpringBootTest(webEnvironment = SpringBootTest.WebEnvironment.RANDOM_PORT)
class WuHanScienceServerApplicationTest {

    @Resource
    lateinit var iZhihuiTripService: IZhihuiTripItemService


    @Test
    fun test() {
//        var randomKey: String = AES.generateRandomKey()

//        randomKey = "4db68cf200d3852a"
//        println(AES.encrypt("jdbc:p6spy:postgresql://prod.tugezigui1.com:23456/pinhui_trip", randomKey))
//        println(AES.encrypt("pinhuitrip", randomKey))
//        println(AES.encrypt("pinhuitrip_AbCdEfGhI_123456778899", randomKey))
        val jedis: Jedis = RedisDS.create().jedis

        iZhihuiTripService.list().forEach { i ->
            jedis.set(i.id, "40");

        }


    }
}