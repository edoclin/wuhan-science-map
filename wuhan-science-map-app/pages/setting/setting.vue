<template>
    <view>
        <u-cell-group>
            <u-cell title="个人信息" label="管理您的用户信息" @click="toOtherPage('user-info')" v-if="userStore.openid !== ''">
            </u-cell>
            <u-cell title="关于我们" label="关于武汉市科协和一张图"></u-cell>
            <u-cell title="退出登录" label="退出当前登录的账号" @click="showLogoutConfirm = true"  v-if="userStore.openid !== ''"></u-cell>
        </u-cell-group>
        <u-modal :show="showLogoutConfirm" title="退出登录" showCancelButton @confirm="logout" 
            @cancel="showLogoutConfirm = false">
            <template>
                确认退出当前登录账号?
            </template>
        </u-modal>
    </view>
</template>

<script setup>
    import {
        logoutApp
    } from '../../api/user';
    import {
        useUserStore
    } from '../../stores/user_store';
    import {
        ref
    } from "vue";

    const userStore = useUserStore()
    const showLogoutConfirm = ref(false)
    const logout = () => {
        logoutApp().then(res => {
            showLogoutConfirm.value = false
            uni.showToast({
                icon: 'success',
                title: '已退出当前账户'
            })
            userStore.updateMobile('')
            userStore.updateOpenid('')
            userStore.updateToken('')
            userStore.updateUserInfo({})
            
            setTimeout(() => {
                uni.switchTab({
                    url:'/pages/user/user'
                })
            }, 1500)
        })
    }
    const toOtherPage = (pageName) => {
        uni.navigateTo({
            url: `/pages/${pageName}/${pageName}`
        })
    }
</script>

<style>

</style>
