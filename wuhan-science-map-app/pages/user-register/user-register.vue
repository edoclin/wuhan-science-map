<template>
    <view style="margin: 0 3%;">
        <u-form labelPosition="top" labelWidth="150" labelAlign="left" :model="registerForm" :rules="registerRules"
            ref="registerFormRef">
            <u-divider text="以下为监护人信息"></u-divider>
            <u-form-item label="姓名" prop="realName" borderBottom ref="realName">
                <u-input v-model="registerForm.realName" placeholder="请输入监护人真实姓名"></u-input>
            </u-form-item>
            <u-form-item label="身份证号" prop="prcRid" borderBottom ref="prcRid">
                <u-input v-model="registerForm.prcRid" placeholder="请输入居民身份证号"></u-input>
            </u-form-item>
            <u-form-item label="联系方式" prop="mobile" borderBottom ref="mobile">
                <u-input v-model="registerForm.mobile" placeholder="请输入联系方式"></u-input>
            </u-form-item>
            <u-form-item label="家庭住址" prop="addressDetail" borderBottom ref="addressDetail">
                <view @click="openMap">
                    <u-input v-model="registerForm.addressDetail" placeholder="请点击选择家庭住址"></u-input>
                </view>
            </u-form-item>
            <u-form-item label="性别" prop="isMale" borderBottom ref="isMale">
                <view style="margin: 0 2%;">
                    <u-radio-group v-model="registerForm.isMale" iconPlacement="left" labelDisabled placement="row">
                        <u-radio activeColor="red" label="男" name="false"></u-radio>
                        <view style="margin-left: 10%;">
                            <u-radio activeColor="green" label="女" name="true"></u-radio>
                        </view>
                    </u-radio-group>
                </view>
            </u-form-item>
            <u-divider text="以下为孩子信息"></u-divider>
            <u-form-item label="孩子姓名" prop="childRealName" borderBottom ref="childRealName">
                <u-input v-model="registerForm.childRealName" placeholder="请输入孩子姓名"></u-input>
            </u-form-item>
            <u-form-item label="孩子身份证号" prop="childPrcRid" borderBottom ref="childPrcRid">
                <u-input v-model="registerForm.childPrcRid" placeholder="请输入孩子居民身份证号"></u-input>
            </u-form-item>
            <u-form-item label="孩子联系方式" prop="childMobile" borderBottom ref="childMobile">
                <u-input v-model="registerForm.childMobile" placeholder="非必填"></u-input>
            </u-form-item>
            <u-form-item label="孩子性别" prop="childIsMale" borderBottom ref="childIsMale">
                <view style="margin: 0 2%;">
                    <u-radio-group v-model="registerForm.childIsMale" iconPlacement="left" labelDisabled
                        placement="row">
                        <u-radio activeColor="red" label="男" name="false"></u-radio>
                        <view style="margin-left: 10%;">
                            <u-radio activeColor="green" label="女" name="true"></u-radio>
                        </view>
                    </u-radio-group>
                </view>
            </u-form-item>
            <u-divider text="操作"></u-divider>
            <u-button type="success" style="margin-bottom: 10vh;" @click="submitRegisterForm">注册</u-button>
            <u-divider text="江城科普一张图"></u-divider>
            <u-gap height="10" bgColor="#fff"></u-gap>
        </u-form>
    </view>
</template>

<script setup>
    import {
        onMounted,
        ref,
        getCurrentInstance
    } from "vue";
    import {
        registerApp,
        decryptMobileData
    } from "../../api/user";
    import {
        useUserStore
    } from '../../stores/user_store';

    const registerFormRef = ref(null)
    const registerForm = ref({
        realName: '',
        prcRid: '',
        mobile: '',
        addressDetail: '',
        latitude: 0,
        longitude: 0,
        isMale: '',
        childName: '',
        childPrcRid: '',
        childMobile: '',
        childIsMale: '',
    })

    onMounted(() => {
        const {
            proxy,
            ctx
        } = getCurrentInstance()
        registerFormRef.value = ctx.$refs.registerFormRef
    })


    const userStore = useUserStore()

    registerForm.value['mobile'] = userStore.mosbile



    const registerRules = ref({
        'realName': {
            type: 'string',
            required: true,
            message: '请填写姓名',
            trigger: ['blur', 'change']
        },
        'prcRid': {
            type: 'string',
            required: true,
            pattern: /^[1-9]\d{7}((0\d)|(1[0-2]))(([0|1|2]\d)|3[0-1])\d{3}$|^[1-9]\d{5}[1-9]\d{3}((0\d)|(1[0-2]))(([0|1|2]\d)|3[0-1])\d{3}([0-9]|X)$/,
            message: '请填写正确的身份证号',
            trigger: ['blur', 'change']
        },
        'mobile': {
            type: 'string',
            required: true,
            pattern: /^(13[0-9]|14[01456879]|15[0-35-9]|16[2567]|17[0-8]|18[0-9]|19[0-35-9])\d{8}$/,
            message: '请填写正确的中国大陆手机号',
            trigger: ['blur', 'change']
        },
        'childMobile': {
            type: 'string',
            required: false,
            pattern: /^(13[0-9]|14[01456879]|15[0-35-9]|16[2567]|17[0-8]|18[0-9]|19[0-35-9])\d{8}$/,
            message: '请填写正确的中国大陆手机号',
            trigger: ['blur', 'change']
        },

    })

    const openMap = () => {
        uni.authorize({
            scope: 'scope.userLocation',
            success() {
                uni.chooseLocation({
                    success({
                        address,
                        latitude,
                        longitude
                    }) {
                        registerForm.value['addressDetail'] = address
                        registerForm.value['latitude'] = latitude
                        registerForm.value['longitude'] = longitude
                    }
                })
            }
        })
    }

    const submitRegisterForm = () => {
        registerFormRef.value.validate().then(res => {
            registerApp(registerForm.value).then(res => {
                uni.showToast({
                    icon: "none",
                    title: '注册成功,即将返回个人中心!'
                })
                userStore.updateOpenid(res.data['openid'])
                res.data['isMale'] = res.data['parentIsMale']
                userStore.updateUserInfo(res.data)

                setTimeout(() => {
                    uni.switchTab({
                        url: '/pages/user/user'
                    })
                }, 1500)
            })
        })
    }
</script>

<style>

</style>
