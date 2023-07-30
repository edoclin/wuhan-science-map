<template>
  <div class="fullscreen text-center q-pa-md flex flex-center">
    <el-result
        title="404"
        sub-title="页面未找到"
    >
      <template #icon>
        <el-avatar shape="square" :size="50"
            src="https://prod-cdn.tugezigui1.com/static/pinhui.trip.logo.trip"
        />
      </template>
      <template #extra>
        <q-btn
            class="q-mt-xl"
            color="white"
            text-color="blue"
            unelevated
            to="/"
            label="返回首页"
            no-caps
        />
      </template>
    </el-result>
  </div>
</template>

<script setup lang="ts">

</script>
