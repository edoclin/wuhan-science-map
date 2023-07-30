<template>
    <view>
        <u-skeleton :rows="60" :loading="skeleton" :title="false">
            <view style="margin: 0 25%;">
                <image :src="lecturerDetail['lecturerPicture']" style="width: 100%; border-radius: 10px;"></image>
            </view>
            <view style="margin: 0 25%; text-align: center; margin-top: 10px; font-weight: 600; font-size: 20px;">
                {{lecturerDetail.lecturerName}}
            </view>
            
            <u-parse :content="lecturerDetail['descRichText']" style="margin: 15px 4%;"></u-parse>
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
        lecturerDetailById
    } from "../../api/lecturer";
    const lecturerDetail = ref({})
    const skeleton = ref(true)
    onLoad((option) => {
        lecturerDetailById(option['id']).then(res => {
            setTimeout(() => {
                skeleton.value = false
            }, 500)
            lecturerDetail.value = res.data
            uni.setNavigationBarTitle({
                title: lecturerDetail.value.lecturerName
            })
        })
    })
</script>

<style>

</style>
