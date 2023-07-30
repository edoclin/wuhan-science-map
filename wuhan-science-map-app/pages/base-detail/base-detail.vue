<template>
	<view>
		<map id="base-map" style="width: 100%;min-height: 300px; max-height: 300px;" :latitude="mapParam.latitude"
			:longitude="mapParam.longitude" :polygons="mapParam.polygons" :scale="mapParam.scale" show-location>
			<view style="margin-top: 273px;display: flex;">
				<cover-image style="width: 55rpx; height: 55rpx;" src='/static/logo.png'>
				</cover-image>
				<view style="text-align: center;margin-top: 10rpx;margin-left: 6rpx; background-color:cornsilk;">
					<view>江城科普一张图</view>
				</view>
			</view>
		</map>
		<u-skeleton :rows="20" :loading="skeleton" v-show="skeleton" :title="false">
		</u-skeleton>
		<u-cell-group v-show="!skeleton">
			<u-cell value="单位名称" :title="baseDetail['baseName']"></u-cell>
			<u-cell value="单位类别" :title="baseDetail['baseType']"></u-cell>
			<u-cell title="详细地址" :label="baseDetail['detailAddress']" rightIcon="map" isLink @click="openLocation">
			</u-cell>
		</u-cell-group>
		<u-collapse @change="changeCollapse" @close="closeCollapse" @open="openCollapse" v-show="!skeleton">
			<u-collapse-item title="单位简介" name="descRichText" :duration="500">
				<template #value>
					<view style="font-size: 12px;color: #aaaaaa;">
						{{collapseStatus['descRichText'] ? '收起' : '展开'}}
					</view>
				</template>
				<view style="margin: 0 1%;">
					<u-parse :content="baseDetail['descRichText']"></u-parse>
				</view>
			</u-collapse-item>
			<u-collapse-item title="参观指南" name="visitTutorial" icon="" :duration="500">
				<template #value>
					<view style="font-size: 12px;color: #aaaaaa;">
						{{collapseStatus['visitTutorial'] ? '收起' : '展开'}}
					</view>
				</template>
				<view style="margin: 0 1%;">
					<u-parse :content="baseDetail['visitTutorial']"></u-parse>
				</view>
			</u-collapse-item>
			<u-collapse-item title="展厅详情" name="halls" icon="" :duration="500">
				<template #value>
					<view style="font-size: 12px;color: #aaaaaa;">
						{{collapseStatus['halls'] ? '收起' : '展开'}}
					</view>
				</template>
				<u-empty mode="data" v-if="!baseDetail['halls']" text="暂时还未更新...">
				</u-empty>
				<u-scroll-list v-else>
					<view v-for="(item, index) in baseDetail['halls']" :key="index" @click="toHallDetail(item.id)">
						<image :src="item.hallLogoUrl"
							style="height: 100px; width: 100px;margin-right: 20px; border-radius: 15px;"></image>
						<view style="width: 100px; text-align: center;">
							{{item.hallName.length >= 5 ? item.hallName.substring(0, 5) + '...' : item.hallName}}
						</view>
					</view>
				</u-scroll-list>
			</u-collapse-item>
			<u-collapse-item title="科普课程" name="courses" icon="" :duration="500">
				<template #value>
					<view style="font-size: 12px;color: #aaaaaa;">
						{{collapseStatus['courses'] ? '收起' : '展开'}}
					</view>
				</template>
				<u-empty mode="data" v-if="!baseDetail['courses']" text="暂时没有课程...">
				</u-empty>
				<u-cell-group v-else>
					<u-cell isLink v-for="(item, id) in baseDetail['courses']" :key="id"
						:url="`/pages/course-detail/course-detail?id=${item.id}`">
						<template #title>
							<view style="color: #777777;">
								{{id + 1}}.{{item.courseName}}
							</view>
						</template>
						<template #value>
							<view style="color: #afafaf; font-size: 12px;">
								详情
							</view>
						</template>
					</u-cell>
				</u-cell-group>
			</u-collapse-item>
			<u-collapse-item title="讲师风采" name="lectures" icon="" :duration="500">
				<template #value>
					<view style="font-size: 12px;color: #aaaaaa;">
						{{collapseStatus['lectures'] ? '收起' : '展开'}}
					</view>
				</template>
				<u-empty mode="data" v-if="!baseDetail['lecturers']" text="暂时还未更新...">
				</u-empty>
				<u-scroll-list v-else>
					<view v-for="(item, index) in baseDetail['lecturers']" :key="index"
						@click="toLecturerDetail(item.id)">
						<image :src="item.lecturerPicture"
							style="height: 100px; width: 100px;margin-right: 20px; border-radius: 15px;"></image>
						<view style="width: 100px; text-align: center;">
							{{item.lecturerName.length >= 5 ? item.lecturerName.substring(0, 5) + '...' : item.lecturerName}}
						</view>
					</view>
				</u-scroll-list>
			</u-collapse-item>
			<u-collapse-item title="最新活动" name="activities" icon="" :duration="500">
				<template #value>
					<view style="font-size: 12px;color: #aaaaaa;">
						{{collapseStatus['activities'] ? '收起' : '展开'}}
					</view>
				</template>
				<u-empty mode="data" v-if="!baseDetail['activities']" text="暂时没有活动...">
				</u-empty>
				<u-scroll-list v-else>
					<view v-for="(item, index) in baseDetail['activities']" :key="index"
						@click="toActivityDetail(item.id)">
						<image :src="item.coverAccessKeyUrl"
							style="height: 100px; width: 100px;margin-right: 20px; border-radius: 15px;"></image>
						<view style="width: 100px; text-align: center;">
							{{item.activityName.length >= 5 ? item.activityName.substring(0, 5) + '...' : item.activityName}}
						</view>
					</view>
				</u-scroll-list>
			</u-collapse-item>
			<u-collapse-item title="友情链接" name="links" icon="" :duration="500">
				<template #value>
					<view style="font-size: 12px;color: #aaaaaa;">
						{{collapseStatus['links'] ? '收起' : '展开'}}
					</view>
				</template>
				<u-empty mode="data" v-if="!baseDetail['links']" text="暂时还未更新...">
				</u-empty>
				<u-cell-group v-else>
					<u-cell v-for="(item, id) in baseDetail['links']" :icon="item.linkLogoUrl" :key="id"
						@click="copyUrl(item.url)">
						<template #title>
							<view style="color: #19be6b;text-align: center;">
								{{item.linkName}}
							</view>
						</template>
					</u-cell>
				</u-cell-group>
			</u-collapse-item>
		</u-collapse>
	</view>
</template>

<script setup>
	import {
		onLoad
	} from "@dcloudio/uni-app"
	import {
		baseDetailById
	} from "../../api/base";
	import {
		reactive,
		ref,
		getCurrentInstance
	} from "vue";

	const baseDetail = ref({})
	const skeleton = ref(true)

	const mapParam = reactive({
		latitude: 30,
		longitude: 110,
		polygons: [],
		scale: 10
	})
	const compass =  ref({
	    direction: 0,
	    strLongitude: '',
	    strLatitude: '',
	    tagLatitude: "",
	    tagLongitude: "",
	    rotate: 0,
	    show: false,
	})
	const map = ref(null)

	const collapseValue = [
		'descRichText',
		'visitTutorial',
		'halls',
		'courses',
		'lectures',
		'activities',
		'links',
	]

	const collapseStatus = ref({
		descRichText: false,
		visitTutorial: false,
		halls: false,
		courses: false,
		lectures: false,
		activities: false,
		links: false,
	})


	onLoad((option) => {
		map.value = uni.createMapContext("base-map")
		baseDetailById(option['id']).then(res => {
			setTimeout(() => {
				skeleton.value = false
			}, 1000)
			baseDetail.value = res.data
			uni.setNavigationBarTitle({
				title: baseDetail.value.baseName
			})
			mapParam.latitude = res.data['centroid']['latitude']
			mapParam.longitude = res.data['centroid']['longitude']
			mapParam.scale = res.data['centroid']['mapScale']
			mapParam.polygons.push({
				points: [],
				strokeColor: "#00ff7fc0",
				fillColor: "#00ff7fc0"
			})
			res.data.polygonGeometry.forEach((item) => {
				mapParam.polygons[0].points.push({
					...item
				})
			})
		})
	})

	const changeCollapse = (e) => {}

	const openCollapse = (name) => {
		collapseStatus.value[name] = true
	}

	const closeCollapse = (name) => {
		collapseStatus.value[name] = false
	}

	const copyUrl = (url) => {
		if (false) {
			uni.setClipboardData({
				data: url,
				success: () => {
					uni.showToast({
						icon: "none",
						title: '链接已复制, 请到浏览器打开!'
					})
				}
			});
		} else {
			uni.navigateTo({
				url: `/pages/user-web-view/user-web-view?url=${url}`
			})
		}

	}

	const openLocation = () => {
		uni.openLocation({
			latitude: baseDetail.value.centroid['latitude'],
			longitude: baseDetail.value.centroid['longitude'],
		})
	}

	const toLecturerDetail = (id) => {
		uni.navigateTo({
			url: `/pages/lecturer-detail/lecturer-detail?id=${id}`
		})
	}

	const toHallDetail = (id) => {
		uni.navigateTo({
			url: `/pages/hall-detail/hall-detail?id=${id}`
		})
	}

	const toActivityDetail = (id) => {
		uni.navigateTo({
			url: `/pages/activity-detail/activity-detail?id=${id}`
		})
	}
</script>

<style>

</style>
