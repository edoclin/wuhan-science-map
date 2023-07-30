import { defineStore } from 'pinia'
import { LocalStorage } from 'quasar'

interface UserInfo {
  mobile: string
  roleKey: string
  roleName: string
}

interface Token {
  name: string
  value: string
}

export const useUserStore = defineStore('user_store', {
  state: () => ({
    token: LocalStorage.getItem('token') || {} as Token,
    userInfo: LocalStorage.getItem('userInfo') || {} as UserInfo
  }),
  getters: {},
  actions: {
    updateToken (token: string) {
      this.token = token
    },
    updateUserInfo (userInfo: UserInfo) {
      this.userInfo = userInfo
    },
  },
  persist: {
    enabled: true
  }
})


