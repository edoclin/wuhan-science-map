import {
    defineStore
} from 'pinia';

export const useUserStore = defineStore('user_store', {
    state: () => {
        return {
            userInfo: uni.getStorageSync('userInfo') || {isNull: true},
            userProfile: uni.getStorageSync('userProfile') || {isNull: true},
            token: uni.getStorageSync('token') || '',
            openid: uni.getStorageSync('openid') || '',
            mobile: uni.getStorageSync('mobile') || ''
        };
    },
    actions: {
        updateUserInfo(userInfo) {
            this.userInfo = userInfo;
            uni.setStorage({
                key: 'userInfo',
                data: userInfo
            })
        },
        updateUserProfile(userProfile) {
            this.userProfile = userProfile;
            uni.setStorage({
                key: 'userProfile',
                data: userProfile
            })
        },
        updateToken(token) {
            this.token = token
            uni.setStorage({
                key: 'token',
                data: token
            })
        },
        updateOpenid(openid) {
            this.openid = openid
            uni.setStorage({
                key: 'openid',
                data: openid
            })
        },
        updateMobile(mobile) {
            this.mobile = mobile
            uni.setStorage({
                key: 'mobile',
                data: mobile
            })
        },
        updateUserToken(data) {
            this.updateOpenid(data.openid)
            this.updateToken(data.token)

        }

    },
});
