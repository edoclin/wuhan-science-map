<template>
    <view>
        <u-popup :show="showCourseResultUpload" :duration="600" closeable @close="closePopup">
            <view>
                <view style="margin-left: 10px;">
                    <u--textarea border="false" v-model="courseResultUploadForm.content" placeholder="说点什么...">
                    </u--textarea>
                </view>
                <view style="margin-left: 15px; margin-bottom: 15px;">
                    <u-upload :fileList="courseResultUploadForm.files" @afterRead="afterRead" @delete="deletePic"
                        multiple accept="media" :maxCount="9"></u-upload>
                </view>
                <view style="position: absolute; bottom: 25px; right: 25px;">
                    <u-button @click="submitCourseResult" text="发布" iconColor="#fff"
                        color="linear-gradient(to right, rgb(66, 83, 216), rgb(213, 51, 186))"></u-button>
                </view>
            </view>
        </u-popup>
        <u-skeleton :loading="skeleton" :animate="true" :rows="40" :title="false">
            <u-cell-group>
                <u-cell v-for="(item, index) in courseResultUploadList" :key="index">
                    <template #label>
                        <view style="font-size: 12px; margin-top: 5px;color: #9091af; margin-bottom: 10px;">
                            课程名称: {{item.courseName}}
                        </view>
                        <view style="font-size: 12px; margin-top: 5px;color: #9091af; margin-bottom: 10px;">
                            所属基地: {{item.baseName}}
                        </view>

                        <view style="font-size: 16px; margin-top: 5px;color: #000000; margin-bottom: 10px;">
                            {{item.accessKey}}
                        </view>
                        <u-album keyName="key" :urls="item.descRichText" multipleSize="calc((99vw - 8 * 6px) / 3)">
                        </u-album>
                        <view
                            style="font-size: 12px; margin-top: 15px;color: #9091af; margin-bottom: 5px; text-align: right;">
                            {{item.updatedTime}}
                        </view>
                    </template>
                </u-cell>
            </u-cell-group>
            <view v-if="courseResultUploadForm.courseId !== '-1'" style="position: absolute; bottom: 25px; right: 25px;">
                <u-button icon="plus" iconColor="#fff"
                    color="linear-gradient(to right, rgb(66, 83, 216), rgb(213, 51, 186))"
                    @click="() => showCourseResultUpload = true"></u-button>
            </view>
        </u-skeleton>

    </view>
</template>

<script setup>
    import {
        ref
    } from "vue";
    import {
        uploadFile
    } from "../../api/cos/request";
    import {
        listUserCourseResultByCourseId,
        postUserCourseResult
    } from "../../api/user";
    const showCourseResultUpload = ref(false)
    const skeleton = ref(true)

    const courseResultUploadList = ref([])

    const courseResultUploadForm = ref({
        content: '',
        files: [],
        courseId: ''
    })
    import {
        onLoad,
        onPullDownRefresh
    } from "@dcloudio/uni-app"

    onLoad((option) => {
        courseResultUploadForm.value.courseId = option['id']
        console.log(courseResultUploadForm.value.courseId);
        listUserCourseResultByCourseId(option['id']).then(res => {
            courseResultUploadList.value = res.data
            setTimeout(() => {
                skeleton.value = false
            }, 500)
        })
    })

    onPullDownRefresh(() => {
        console.log("onPullDownRefresh");
        skeleton.value = true
        listUserCourseResultByCourseId(courseResultUploadForm.value.courseId).then(res => {
            courseResultUploadList.value = res.data
            setTimeout(() => {
                skeleton.value = false
                uni.stopPullDownRefresh()
            }, 500)
        })
    })

    const afterRead = ({
        file
    }) => {
        file.forEach(item => {
            courseResultUploadForm.value.files.push({
                url: item.url,
                thumb: item.thumb,
                type: item.type
            })
        })
    }

    const deletePic = (e) => {

    }

    const closePopup = (e) => {
        showCourseResultUpload.value = false
    }

    const submitCourseResult = () => {
        let counter = 0
        let Keys = []
        courseResultUploadForm.value.files.forEach(async (item) => {
            let res = await uploadFile(item)
            if (res.statusCode === 200) {
                counter++
                let splits = res.Location.split('/')
                splits.splice(0, 1)
                item["key"] = splits.join("/")
            }
            if (counter === courseResultUploadForm.value.files.length) {
                postUserCourseResult({
                    accessKey: courseResultUploadForm.value.content,
                    descRichText: courseResultUploadForm.value.files,
                    courseId: courseResultUploadForm.value.courseId
                }).then(res => {
                    if (res.code === 2000) {
                        uni.showToast({
                            icon: "success",
                            title: res.data
                        })
                        setTimeout(() => {
                            courseResultUploadForm.value.files = []
                            courseResultUploadForm.value.content = []
                            showCourseResultUpload.value = false
                        }, 1200)
                    }
                })
            }
        })

    }

    const urls2 = ref([
        'https://cdn.uviewui.com/uview/album/1.jpg',
        'https://cdn.uviewui.com/uview/album/2.jpg',
        'https://cdn.uviewui.com/uview/album/3.jpg',
        'https://cdn.uviewui.com/uview/album/4.jpg',
        'https://cdn.uviewui.com/uview/album/5.jpg',
        'https://cdn.uviewui.com/uview/album/6.jpg',
        'https://cdn.uviewui.com/uview/album/7.jpg',
        'https://cdn.uviewui.com/uview/album/8.jpg',
        'https://cdn.uviewui.com/uview/album/9.jpg',
        'https://cdn.uviewui.com/uview/album/10.jpg',
    ])
</script>

<style>

</style>
