import { boot } from 'quasar/wrappers'
import axios, { AxiosInstance } from 'axios'
import { useUserStore } from 'src/stores/user_store'
import { ElMessage } from 'element-plus'
import { useMapState } from 'src/stores'
import { router } from 'src/router'
import { LocalStorage } from 'quasar'
import { mapActions } from 'pinia'
import { copyToClipboard } from 'quasar'

declare module '@vue/runtime-core' {
  interface ComponentCustomProperties {
    $axios: AxiosInstance;
  }
}


// TODO
const prodUrl = ''
const devUrl = 'http://localhost:8080'
const api = axios.create({
  baseURL: process.env.NODE_ENV === 'production' ? `${prodUrl}/server` : `${devUrl}/server`
})

const amapRequest = axios.create({
  baseURL: 'https://restapi.amap.com/v3/'
})

api.interceptors.response.use(response => {
  if (response.data.code === 2000) {
    return response.data
  }
  if (response.data.code >= 5000) {
    ElMessage({
      type: 'error',
      message: response.data.message
    })
    let log = {
      timestamp: response.data.timestamp,
      mobile: '当前登录的账号(若未登录则忽略)',
      errInfo: response.data.message,
      contact: '您的联系方式',
      trigger: '文字描述如何触发该bug'
    }
    copyToClipboard(JSON.stringify(log)).then(() => {
      console.log("错误信息已复制")
    })
    if (response.data.code === 5100) {
      const userAction = mapActions(useUserStore,
        [
          'updateToken',
          'updateUserInfo'])
      // @ts-ignore
      userAction.updateUserInfo({})
      // @ts-ignore
      userAction.updateToken({})
      LocalStorage.remove('token')
      LocalStorage.remove('userInfo')

      router.push({
        path: '/cms/login'
      }).catch(err => {
        console.log(err)
      })
    }
    return Promise.reject(response.data)
  }
  return response
}, error => {
  return Promise.reject(error)
})

export const GET = (url: string, params: {} = {}) => {
  return new Promise((resolve, reject) => {
    api.get(url, { params }
    ).then(res => {
      resolve(res)
    }).catch(err => {
      reject(err)
    })
  })
}

export const POST = (url: string, params: {}) => {
  return new Promise((resolve, reject) => {
    api.post(url, { ...params })
      .then(res => {
        resolve(res)
      })
      .catch(err => {
        reject(err)
      })
  })
}

export const PUT = (url: string, params: {}) => {
  return new Promise((resolve, reject) => {
    api.put(url, params)
      .then(res => {
        resolve(res)
      })
      .catch(err => {
        reject(err)
      })
  })
}

export const DELETE = (url: string, params: {}) => {
  return new Promise((resolve, reject) => {
    api.delete(url, { data: params })
      .then(res => {
        resolve(res)
      })
      .catch(err => {
        reject(err)
      })
  })
}
export default boot(({
  app,
  router
}) => {
  const {
    token
  } = useMapState(useUserStore, ['token'])
  api.interceptors.request.use(config => {
    if (token.value.name !== undefined) {
      // @ts-ignore
      config.headers[token.value.name] = token.value.value
    }
    return config
  }, error => {
    return Promise.reject(error)
  })
  app.config.globalProperties.$axios = axios
  app.config.globalProperties.$api = api
})
export { api, amapRequest}


