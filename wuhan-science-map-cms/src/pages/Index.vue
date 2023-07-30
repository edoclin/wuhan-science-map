<template>
  <div>
    <el-config-provider :locale="zhCn">
      <router-view/>
    </el-config-provider>
  </div>
</template>

<script setup>
import zhCn from 'element-plus/dist/locale/zh-cn.mjs'
import { useRouter } from 'vue-router'
// import { webCheck } from 'src/api/user'

const router = useRouter()

router.push({
  path: '/cms'
})
//
// webCheck().then(res => {
//   router.push({
//     path: '/cms'
//   })
// }).catch(err => {
//   console.log(err)
//   router.push({
//     path: '/cms/login'
//   })
// })
</script>
