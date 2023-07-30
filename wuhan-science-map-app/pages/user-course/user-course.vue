<template>
    <view>
        <u-sticky>
            <u-subsection :list="['报名课程', '收藏课程']" :current="tab" @change="(index) => tab = index"></u-subsection>
        </u-sticky>
        <u-skeleton :rows="60" :loading="skeleton" :title="false" v-if="tab === 0">
            <u-swipe-action>
                <u-swipe-action-item :disabled="item.finished" :options="[{text: '取消报名', style: {
                            backgroundColor: '#ff3001',
                        }}]" v-for="(item, index) in userRegisterCourseList" :key="item.id">
                    <view>
                        <u-cell-group>
                            <u-cell
                                :url="`/pages${item.finished ? '/course-upload-result/course-upload-result' : '/course-detail/course-detail'}?id=${item.id}`">
                                <template #title>
                                    <view style="font-weight: 550;">
                                        {{item.courseName}}
                                    </view>
                                </template>
                                <template #label>
                                    <view style="font-size: 12px; margin-top: 10px;color: #9091af;">
                                        所属基地: {{item.baseName}}
                                    </view>
                                    <view style="font-size: 12px; margin-top: 5px;color: #9091af;">
                                        报名时间: {{item.createdTime}}
                                    </view>
                                    <view style="font-size: 12px; margin-top: 5px;color: #9091af;">
                                        开课时间: {{item.startingTime}}
                                    </view>
                                </template>
                                <template #value>
                                    <view style="font-size: 12px; margin-top: 5px;color: #9091af;">
                                        <u-icon :name="item.finished? 'file-text' : 'arrow-right-double'"
                                            :label="item.finished ? '成果展示' : '课程详情'" labelPos="left" labelSize="12">
                                        </u-icon>
                                    </view>
                                </template>
                            </u-cell>
                        </u-cell-group>
                    </view>
                </u-swipe-action-item>
            </u-swipe-action>
        </u-skeleton>

        <u-skeleton :rows="60" :loading="skeleton" :title="false" v-if="tab === 1">
            <u-swipe-action>
                <u-swipe-action-item :options="[{text: '取消收藏', style: {
                            backgroundColor: '#fd6b14',
                        }}]" v-for="(item, index) in userFavoriteCourseList" :key="item.id">
                    <view>
                        <u-cell-group>
                            <u-cell :url="`/pages/course-detail/course-detail?id=${item.id}`">
                                <template #title>
                                    <view style="font-weight: 550;">
                                        {{item.courseName}}
                                    </view>
                                </template>
                                <template #label>
                                    <view style="font-size: 12px; margin-top: 10px;color: #9091af;">
                                        所属基地: {{item.baseName}}
                                    </view>
                                    <view style="font-size: 12px; margin-top: 5px;color: #9091af;">
                                        收藏时间: {{item.createdTime}}
                                    </view>
                                </template>
                                <template #value>
                                    <view style="font-size: 12px; margin-top: 5px;color: #9091af;">
                                        <u-icon name="arrow-right-double" label="课程详情" labelPos="left" labelSize="12">
                                        </u-icon>
                                    </view>
                                </template>
                            </u-cell>
                        </u-cell-group>
                    </view>
                </u-swipe-action-item>
            </u-swipe-action>
        </u-skeleton>
    </view>
</template>

<script setup>
    import {
        ref
    } from "vue";
    import {
        listUserFavoriteCourseByUserId,
        listUserRegisterCourseByUserId
    } from "../../api/user";

    import {
        onLoad
    } from "@dcloudio/uni-app"

    const tab = ref(0)

    onLoad((option) => {
        tab.value = parseInt(option['tab'])
    })
    const userFavoriteCourseList = ref([])
    const userRegisterCourseList = ref([])
    const skeleton = ref(true)
    listUserRegisterCourseByUserId().then(res => {
        userRegisterCourseList.value = res.data
        setTimeout(() => {
            skeleton.value = false
        }, 500)

    })

    listUserFavoriteCourseByUserId().then(res => {
        userFavoriteCourseList.value = res.data
        setTimeout(() => {
            skeleton.value = false
        }, 500)
    })
</script>

<style>

</style>
