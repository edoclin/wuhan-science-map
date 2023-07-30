package wuhan.science.server.controller;

import cn.dev33.satoken.annotation.SaCheckLogin
import cn.dev33.satoken.secure.BCrypt
import cn.dev33.satoken.stp.StpUtil
import cn.hutool.core.bean.BeanUtil
import cn.hutool.core.util.ByteUtil
import cn.hutool.core.util.DesensitizedUtil
import cn.hutool.http.HttpUtil
import cn.hutool.json.JSONUtil
import com.baomidou.mybatisplus.extension.kotlin.KtQueryWrapper
import com.baomidou.mybatisplus.extension.plugins.pagination.Page
import org.bouncycastle.jce.provider.BouncyCastleProvider
import org.springframework.beans.factory.annotation.Value
import org.springframework.web.bind.annotation.*
import wuhan.science.server.entity.*
import wuhan.science.server.service.IUserFavoriteCourseService
import wuhan.science.server.service.IUserRegisterCourseService
import wuhan.science.server.service.IUserService
import wuhan.science.server.util.database.Batch
import wuhan.science.server.util.database.QueryParam
import wuhan.science.server.util.database.QueryUtil
import wuhan.science.server.util.date.DateUtil
import wuhan.science.server.util.result.ResultBean
import wuhan.science.server.util.result.ResultUtil
import wuhan.science.server.util.system.Configuration
import wuhan.science.server.util.view.ViewUtil
import wuhan.science.server.view.common.AppSession
import wuhan.science.server.view.common.AppUserInfo
import wuhan.science.server.view.common.WxBizDataCrypt
import wuhan.science.server.view.common.WxMobile
import java.nio.charset.Charset
import java.security.AlgorithmParameters
import java.security.Security
import java.util.*
import javax.annotation.Resource
import javax.crypto.Cipher
import javax.crypto.spec.IvParameterSpec
import javax.crypto.spec.SecretKeySpec
import javax.validation.Valid
import javax.validation.constraints.DecimalMin
import javax.validation.constraints.NotBlank

/**
 * <p>
 * 用户表 前端控制器
 * </p>
 *
 * @author edoclin
 * @since 2023-02-18
 */
@RestController
@RequestMapping("/server/user")
class UserController {
    @Resource
    lateinit var customConfiguration: Configuration

    @Resource
    lateinit var iUserService: IUserService

    @Resource
    lateinit var iUserFavoriteCourseService: IUserFavoriteCourseService

    @Resource
    lateinit var iUserRegisterCourseService: IUserRegisterCourseService

    @Value("\${custom.app.wx.app-id}")
    lateinit var appId: String

    @Value("\${custom.app.wx.app-secret}")
    lateinit var appSecret: String


    @RequestMapping(method = [RequestMethod.POST], path = ["/cms"])
    fun post4Cms(@RequestBody @Valid form: WebPostUser): ResultBean {
        iUserService.getOne(KtQueryWrapper(User::class.java).eq(User::mobile, form.mobile))?.let {
            return ResultUtil.error("手机号已注册")
        } ?: let {
            val insert = User()
            BeanUtil.copyProperties(form, insert)

            insert.password = BCrypt.hashpw(form.password)
            insert.roleKey = customConfiguration.roles[0]
            iUserService.save(insert).takeIf { it }?.let {
                return ResultUtil.ok("注册成功")
            }
            return ResultUtil.error("参数错误")
        }
    }

    @RequestMapping(method = [RequestMethod.POST], path = ["/cms/{current}/{limit}"])
    fun list4Cms(
        @PathVariable @DecimalMin("1") current: Number,
        @PathVariable @DecimalMin("1") limit: Number,
        @RequestBody queryParam: QueryParam
    ): ResultBean {
        val result = mutableListOf<WebUser>()
        val page = Page<User>(current.toLong(), limit.toLong())
        val wrapper = QueryUtil.buildQueryWrapper<User>(queryParam)
        val count = iUserService.count(wrapper)

        iUserService.page(
            page, QueryUtil.orderBy(wrapper, queryParam)
        ).records.mapTo(result) {
            val item = WebUser()
            BeanUtil.copyProperties(it, item)
            item.status = it.status?.desc
            item.createdTime = DateUtil.yyyyMMdd(it.createdTime)
            item.updatedTime = DateUtil.yyyyMMddHHmm(it.updatedTime)
            item
        }
        return ResultUtil.ok(result, count)
    }

    @RequestMapping(method = [RequestMethod.DELETE], path = ["/cms/batch"])
    fun deleteBatch4Cms(@RequestBody @NotBlank batch: Batch): ResultBean {
        iUserService.removeBatchByIds(batch.ids).takeIf { it }?.let {
            batch.ids?.forEach { id ->
                iUserFavoriteCourseService.remove(
                    KtQueryWrapper(UserFavoriteCourse::class.java).eq(
                        UserFavoriteCourse::userId, id
                    )
                )
                iUserRegisterCourseService.remove(
                    KtQueryWrapper(UserRegisterCourse::class.java).eq(
                        UserRegisterCourse::userId, id
                    )
                )
            }
            return ResultUtil.ok("删除成功")
        }
        return ResultUtil.error("参数无效")
    }

    @RequestMapping(method = [RequestMethod.PUT], path = ["cms"])
    fun update4Cms(@RequestBody @Valid form: WebPutUser): ResultBean {
        val update = User()
        BeanUtil.copyProperties(form, update)
        iUserService.updateById(update).takeIf { it }?.let {
            return ResultUtil.ok("更新成功")
        }
        return ResultUtil.error("对象不存在")
    }

    @RequestMapping(method = [RequestMethod.GET], path = ["/cms/conditions"])
    fun conditions4Cms(): ResultBean {
        return ResultUtil.ok(ViewUtil.queryCondition<WebUser>())
    }

    @RequestMapping(method = [RequestMethod.GET], path = ["/cms/table-columns"])
    fun tableColumns4Cms(): ResultBean {
        return ResultUtil.ok(ViewUtil.tableColumns<WebUser>())
    }

    @RequestMapping(method = [RequestMethod.GET], path = ["/cms/selector"])
    fun listSelector4Cms(): ResultBean {
        val result = mutableListOf<WebUser>()
        iUserService.list(KtQueryWrapper(User::class.java).select()).mapTo(result) {
            val item = WebUser()
            // todo: action
            item
        }
        return ResultUtil.ok(result)
    }


    @RequestMapping(method = [RequestMethod.GET], path = ["/app/login/wx/{code}"])
    fun appLoginByWx(@PathVariable code: String): ResultBean {
        val code2SessionUrl =
            "https://api.weixin.qq.com/sns/jscode2session?appid=$appId&secret=$appSecret&js_code=$code&grant_type=authorization_code"
        JSONUtil.toBean(HttpUtil.get(code2SessionUrl), AppSession::class.java).takeIf { BeanUtil.isNotEmpty(it) }
            ?.let { wxSession ->
                iUserService.getOne(KtQueryWrapper(User::class.java).eq(User::wechatUid, wxSession.openid))
                    .takeIf { BeanUtil.isNotEmpty(it) }?.let { user ->
                        println("ASDASDAS")
                        StpUtil.login(user.wechatUid)
                        println("ASDASDAS")
                        StpUtil.getSession().set("wx-session", JSONUtil.toJsonStr(wxSession))
                        val appUserInfo = AppUserInfo()
                        BeanUtil.copyProperties(user, appUserInfo)
                        appUserInfo.token = StpUtil.getTokenValue()
                        appUserInfo.openid = wxSession.openid

                        val child = iUserService.getOne(KtQueryWrapper(User::class.java).eq(User::guardianId, user.id))

                        BeanUtil.copyProperties(user, appUserInfo)

                        appUserInfo.childRealName = child.realName
                        appUserInfo.childMobile = child.mobile
                        appUserInfo.childPrcRid = child.prcRid
                        appUserInfo.childIsMale = child.male.toString()

                        appUserInfo.mobile = DesensitizedUtil.mobilePhone(appUserInfo.mobile)
                        appUserInfo.prcRid = DesensitizedUtil.idCardNum(appUserInfo.prcRid, 6, 4)
                        appUserInfo.parentIsMale = user.male.toString()
                        appUserInfo.childMobile = DesensitizedUtil.mobilePhone(appUserInfo.childMobile)
                        appUserInfo.childPrcRid = DesensitizedUtil.idCardNum(appUserInfo.childPrcRid, 6, 4)


                        appUserInfo.pureMobile = user.mobile
                        appUserInfo.purePrcRid = user.prcRid

                        appUserInfo.pureChildMobile = child.mobile
                        appUserInfo.pureChildPrcRid = child.prcRid
                        return ResultUtil.ok(appUserInfo)
                    } ?: let {
                    StpUtil.login(wxSession.openid)
                    StpUtil.getSession().set("wx-session", JSONUtil.toJsonStr(wxSession))
                    val appUserInfo = AppUserInfo()
                    appUserInfo.token = StpUtil.getTokenValue()
                    appUserInfo.openid = wxSession.openid
                    return ResultUtil.needRegister(appUserInfo)
                }
            }
        return ResultUtil.error("参数错误")
    }

    @RequestMapping(method = [RequestMethod.POST], path = ["/app/decryptData"])
    fun decryptData(@RequestBody wxBizDataCrypt: WxBizDataCrypt): ResultBean {
        val dataByte = Base64.getDecoder().decode(wxBizDataCrypt.encryptedData)
        val sessionKey =
            JSONUtil.toBean(StpUtil.getSession()["wx-session"].toString(), AppSession::class.java).session_key
        var keyByte = Base64.getDecoder().decode(sessionKey)
        val ivByte = Base64.getDecoder().decode(wxBizDataCrypt.iv)
        // 如果密钥不足16位，那么就补足.这个if中的内容很重要
        val base = 16;
        if (keyByte.size % base != 0) {
            val groups = keyByte.size / base + 1;
            val temp = ByteArray(groups * base)
            Arrays.fill(temp, ByteUtil.intToByte(0))
            System.arraycopy(keyByte, 0, temp, 0, keyByte.size);
            keyByte = temp;
        }
        Security.addProvider(BouncyCastleProvider());
        val cipher = Cipher.getInstance("AES/CBC/PKCS7Padding");
        val spec = SecretKeySpec(keyByte, "AES");
        val parameters = AlgorithmParameters.getInstance("AES");
        parameters.init(IvParameterSpec(ivByte));
        cipher.init(Cipher.DECRYPT_MODE, spec, parameters);// 初始化
        val resultByte = cipher.doFinal(dataByte);
        if (null != resultByte && resultByte.isNotEmpty()) {
            val result = JSONUtil.toBean(String(resultByte, Charset.forName("UTF-8")), WxMobile::class.java)
            return ResultUtil.ok(result);
        }
        return ResultUtil.error("参数错误")
    }

    @RequestMapping(method = [RequestMethod.POST], path = ["/app/register/wx"])
    fun register4Wx(@RequestBody @Valid form: AppUserInfoForm): ResultBean {
        val parent = User()
        val child = User()

        val appSession = JSONUtil.toBean(StpUtil.getSession()["wx-session"].toString(), AppSession::class.java)

        parent.realName = form.realName
        parent.male = form.isMale
        parent.addressDetail = form.addressDetail
        parent.mobile = form.mobile
        parent.wechatUid = appSession.openid
        parent.prcRid = form.prcRid
        parent.child = false
        parent.roleKey = customConfiguration.roles[0].split(".")[0]

        iUserService.save(parent).takeIf { it }?.let {
            child.realName = form.childRealName
            child.male = form.childIsMale
            child.mobile = form.childMobile
            child.prcRid = form.childPrcRid
            child.child = true
            child.guardianId = parent.id
            child.roleKey = customConfiguration.roles[0].split(".")[0]

            iUserService.save(child).takeIf { it }?.let {
                val appUserInfo = AppUserInfo()
                BeanUtil.copyProperties(parent, appUserInfo)

                appUserInfo.childRealName = child.realName
                appUserInfo.childMobile = child.mobile
                appUserInfo.childPrcRid = child.prcRid
                appUserInfo.childIsMale = child.male.toString()

                appUserInfo.mobile = DesensitizedUtil.mobilePhone(appUserInfo.mobile)
                appUserInfo.prcRid = DesensitizedUtil.idCardNum(appUserInfo.prcRid, 6, 4)

                appUserInfo.parentIsMale = parent.male.toString()
                appUserInfo.childMobile = DesensitizedUtil.mobilePhone(appUserInfo.childMobile)
                appUserInfo.childPrcRid = DesensitizedUtil.idCardNum(appUserInfo.childPrcRid, 6, 4)

                appUserInfo.pureMobile = parent.mobile
                appUserInfo.purePrcRid = parent.prcRid
                appUserInfo.pureChildMobile = child.mobile
                appUserInfo.pureChildPrcRid = child.prcRid
                return ResultUtil.ok(appUserInfo)
            }
        }
        return ResultUtil.error()
    }

    @RequestMapping(method = [RequestMethod.GET], path = ["/app/logout/wx"])
    fun logout4Wx(): ResultBean {
        StpUtil.logout()
        return ResultUtil.ok()
    }

    @RequestMapping(method = [RequestMethod.GET], path = ["/app/checkToken"])
    @SaCheckLogin
    fun checkToken4App(): ResultBean {
        return ResultUtil.ok()
    }

    @RequestMapping(method = [RequestMethod.PUT], path = ["/app/wx"])
    fun updateUserInfo(@RequestBody @Valid form: AppUserInfoForm): ResultBean {

        val one =
            iUserService.getOne(KtQueryWrapper(User::class.java).eq(User::wechatUid, StpUtil.getLoginIdAsString()))

        takeIf { BeanUtil.isNotEmpty(one) }?.let {
            one.realName = form.realName
            one.male = form.isMale
            one.addressDetail = form.addressDetail
            one.mobile = form.mobile
            one.prcRid = form.prcRid

            iUserService.updateById(one).takeIf { it }?.let {
                val child = iUserService.getOne(KtQueryWrapper(User::class.java).eq(User::guardianId, one.id))
                takeIf { BeanUtil.isNotEmpty(child) }?.let {
                    child.realName = form.childRealName
                    child.male = form.childIsMale
                    child.mobile = form.childMobile
                    child.prcRid = form.childPrcRid

                    iUserService.updateById(child).takeIf { it }?.let {
                        val appUserInfo = AppUserInfo()
                        BeanUtil.copyProperties(one, appUserInfo)

                        appUserInfo.childRealName = child.realName
                        appUserInfo.childMobile = child.mobile
                        appUserInfo.childPrcRid = child.prcRid
                        appUserInfo.childIsMale = child.male.toString()

                        appUserInfo.mobile = DesensitizedUtil.mobilePhone(appUserInfo.mobile)
                        appUserInfo.prcRid = DesensitizedUtil.idCardNum(appUserInfo.prcRid, 6, 4)

                        appUserInfo.parentIsMale = one.male.toString()
                        appUserInfo.childMobile = DesensitizedUtil.mobilePhone(appUserInfo.childMobile)
                        appUserInfo.childPrcRid = DesensitizedUtil.idCardNum(appUserInfo.childPrcRid, 6, 4)

                        appUserInfo.pureMobile = one.mobile
                        appUserInfo.purePrcRid = one.prcRid
                        appUserInfo.pureChildMobile = child.mobile
                        appUserInfo.pureChildPrcRid = child.prcRid
                        return ResultUtil.ok(appUserInfo)
                    }
                }
            }
        }
        return ResultUtil.error()
    }
}




