<template>
    <view>
        <u-skeleton :rows="60" :loading="JSON.stringify(activityDetail) === '{}'" :title="false">
            <view style="margin: 0 25%;">
                <image :src="activityDetail['coverAccessKeyUrl']" style="width: 100%; border-radius: 10px;"></image>
            </view>
            <view style="margin: 0 25%; text-align: center; margin-top: 10px; font-weight: 600; font-size: 20px;">
                {{activityDetail.activityName}}
                <view style="color: #777777; font-size: 12px;font-weight: 400; margin-top: 5px;">
                    {{activityDetail.updatedTime}}
                </view>

            </view>

            <u-parse :content="activityDetail['descRichText']" style="margin: 15px 4%;"></u-parse>
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
        activityDetailById
    } from "../../api/activity";
    const activityDetail = ref({})
    const skeleton = ref(true)
    onLoad((option) => {
        activityDetailById(option['id']).then(res => {
            setTimeout(() => {
                skeleton.value = false
            }, 500)
            activityDetail.value = res.data
            uni.setNavigationBarTitle({
                title: activityDetail.value.activityName
            })
        })
    })
</script>

<style>

</style>
