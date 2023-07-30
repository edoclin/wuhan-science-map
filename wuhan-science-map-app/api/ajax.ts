// ajax.js

import ajax from '@/uni_modules/u-ajax'

export const baseURL = `http://localhost:8080/server`
// export const baseURL = `http://172.27.25.36:28080/server`
// export const baseURL = `https://science.tugezigui1.com:20443/server`

const instance = ajax.create({
    baseURL: baseURL,
    timeout: 60000
})

instance.interceptors.request.use(
    config => {
        // 在发送请求前做些什么
        config.header['token'] = uni.getStorageSync('token')
        return config
    },
    error => {
        // 对请求错误做些什么
        return Promise.reject(error)
    }
)

// 添加响应拦截器
instance.interceptors.response.use(
    response => {
        // 对响应数据做些什么
        if (response.statusCode === 200) {
            if (response.data.code === 5100) {
                uni.showToast({
                    icon:'error',
                    title: response.data.message
                })
                
                uni.$emit('clean-user-info', {})
                setTimeout(() => {
                    uni.switchTab({
                        url: '/pages/user/user'
                    })
                }, 3000)
            }
            return response.data
        } else {
            uni.showToast({
                icon: 'error',
                title: response.data.message
            })
            return Promise.reject(response.data)
        }
    },
    error => {
        // 对响应错误做些什么
        return Promise.reject(error)
    }
)

// 导出 create 创建后的实例
export default instance