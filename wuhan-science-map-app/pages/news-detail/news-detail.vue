<template>
    <view>
        <u-skeleton :loading="JSON.stringify(newsDetail) === '{}'" :animate="true" :rows="40" :title="false">
            <u--image :showLoading="true" :src="newsDetail['coverAccessKeyUrl']" width="100vw" height="30vh"></u--image>
            <u-cell>
                <template #title>
                    <view style="font-weight: 600;font-size: 20px;">
                        {{newsDetail['newsTitle']}}
                    </view>
                </template>
                <template #label>
                    <view style="margin-top: 8px;font-size: 12px; color: #b2b2b2;">
                        作&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;者: {{newsDetail['creator']}}
                        <br>
                        <view style="margin-top: 5px;"></view>
                        发布时间: {{newsDetail['updatedTime']}}
                    </view>
                </template>
            </u-cell>
            <u-parse :content="newsDetail['contentRichText']" style="margin: 0 4%;"></u-parse>
        </u-skeleton>
    </view>
</template>

<script setup>
    import {
        onLoad
    } from "@dcloudio/uni-app"
    import {
        newsDetailById
    } from "../../api/news";
    import {
        ref
    } from "vue";
    const newsDetail = ref({})
    onLoad((option) => {
        newsDetailById(option['id']).then(res => {
            setTimeout(() => {
                newsDetail.value = res.data
                uni.setNavigationBarTitle({
                    title: newsDetail.value.newsTitle
                })
            }, 500)
            
        })
    })
</script>
<style>

</style>
