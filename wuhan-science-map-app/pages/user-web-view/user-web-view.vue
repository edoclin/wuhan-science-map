<template>
    <view>
        <web-view :src="externalUrl"></web-view>
    </view>
</template>

<script setup>
    import {
        onLoad
    } from "@dcloudio/uni-app"
    import {
        ref
    } from "vue";
    const externalUrl = ref('')
    onLoad((option) => {
        externalUrl.value = option['url']
    })
</script>

<style>

</style>
