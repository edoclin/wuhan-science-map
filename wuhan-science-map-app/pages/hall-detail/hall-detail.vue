<template>
    <view>
        <u-skeleton :rows="60" :loading="JSON.stringify(hallDetail) === '{}'" :title="false">
            <view style="margin: 0 25%;">
                <image :src="hallDetail['hallLogoUrl']" style="width: 100%; border-radius: 10px;"></image>
            </view>
            <view style="margin: 0 25%; text-align: center; margin-top: 10px; font-weight: 600; font-size: 20px;">
                {{hallDetail.hallName}}
            </view>

            <u-parse :content="hallDetail['descRichText']" style="margin: 15px 4%;"></u-parse>
        </u-skeleton>
    </view>
</template>

<script setup>
    import {
        onLoad
    } from "@dcloudio/uni-app"
    import {
        ref
    } from "vue";
    import {
        hallDetailById
    } from "../../api/hall";

    const hallDetail = ref({})

    onLoad((option) => {
        hallDetailById(option['id']).then(res => {
            setTimeout(() => {
                hallDetail.value = res.data
                uni.setNavigationBarTitle({
                    title: hallDetail.value.hallName
                })
            }, 500)

        })
    })
</script>

<style>

</style>
