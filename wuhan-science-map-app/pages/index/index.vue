<template>
	<view>

		<u-datetime-picker ref="datetimePicker" :show="true" v-model="testValue" mode="date"
			:filter="filterDataTime">
		</u-datetime-picker>
		<!-- <uni-data-picker ref="uPicker" v-show="showPicker" placeholder="请选择基地所属区域" popup-title="请选择基地所属区域"
            :localdata="regions" :map="{text:'regionName',value:'code'}" @popupclosed="popClosedHandle"
            @nodeclick="nodeClickHandle">
        </uni-data-picker>
        <u-popup :show="showSearchPopup && !showPicker" mode="top" @close="() => showSearchPopup = false">
            <u-search searchIconSize="16" searchIconColor="#57a8dd" searchIcon="map" :label="currentRegion"
                shape="square" :clearabled="true" placeholder="请输入基地名称搜索" :showAction="false" v-model="searchKeyword"
                @change="handleSearch">
            </u-search>
            <u-loading-icon :show="searchLoding"></u-loading-icon>
            <u-divider v-if="searchKeyword === ''" text="请输入关键字搜索"></u-divider>
            <u-empty v-else-if="searchResult.length === 0 && !searchLoding" mode="search"
                icon="http://cdn.uviewui.com/uview/empty/search.png" />
            <view v-if="searchResult.length !== 0 && !searchLoding">
                <view style="margin: 0 1%;" v-for="(item, index) in searchResult" :key="index"
                    @click="toBaseDetail(item.id)">
                    <u-cell size="large">
                        <template #title>
                            <view style="font-weight: 550; font-size: 16px;color: #000000;">
                                {{item.baseName}}
                            </view>
                        </template>
                        <template #value>
                            <view style="width: 20vw;">
                                <u-tag plain @click="showDetailBaseType(item.baseType)"
                                    :borderColor="baseTypeTags[item.baseType]"
                                    :text="item.baseType.length > 5 ? item.baseType.substring(0, 4) + '...' : item.baseType"
                                    size="mini" :color="baseTypeTags[item.baseType]">
                                </u-tag>
                            </view>
                        </template>
                        <template #label>
                            <view style="margin-top: 4px; font-weight: 400; font-size: 12px;color: #777777;">
                                {{item.detailAddress}}
                            </view>
                        </template>
                        <template #icon>
                            <u-avatar shape="circle" size="60" :src="item.baseLogoUrl"
                                customStyle="margin: -3px 5px -3px 0">
                            </u-avatar>
                        </template>
                    </u-cell>
                </view>
                <u-divider text="已全部加载"></u-divider>
            </view>
        </u-popup>

        <u-sticky>
            <u-search v-if="!showPicker" @clickIcon="chooseRegion" searchIconSize="16" searchIconColor="#57a8dd"
                searchIcon="map" :label="currentRegion" shape="square" @focus="openSearchPopup" :clearabled="true"
                placeholder="请输入基地名称搜索" :showAction="false">
            </u-search>
        </u-sticky>
        <u-swiper height="28vh" indicator indicatorMode="line" :list="swiperList" @click="clickSwiper"
            keyName="carouselImageAccessKeyUrl" showTitle>
        </u-swiper>
        <u-cell style="margin-top: 3px;">
            <template #icon>
                <u-icon name="list-dot" color="#4fa0d7" size="28"></u-icon>
            </template>
            <template #title>
                <view style="font-weight: 600;color: #4fa0d7;font-size: 18px;">
                    科普基地
                </view>
            </template>
            <template #value>
                <view style="width: 35vw;">
                    <uni-data-select @change="filterBaseType" placeholder="基地类型筛选" :localdata="baseTypes" />
                </view>
            </template>
        </u-cell>
        <u-gap height="3" bgColor="#ebebec"></u-gap>
        <u-skeleton :loading="skeleton" :animate="true" :rows="20" :title="false">
            <u-empty v-if="baseInfo.length === 0" mode="list" icon="http://cdn.uviewui.com/uview/empty/list.png"
                text="暂时没有更多科普场馆...">
            </u-empty>
            <view v-else style="margin: 0 1%;" v-for="(item, index) in baseInfo" :key="index"
                @click="toBaseDetail(item.id)">
                <u-cell size="large">
                    <template #title>
                        <view style="font-weight: 550; font-size: 16px;color: #000000;">
                            {{item.baseName}}
                        </view>
                    </template>
                    <template #value>
                        <view style="width: 20vw;">
                            <u-tag plain @click="showDetailBaseType(item.baseType)"
                                :borderColor="baseTypeTags[item.baseType]"
                                :text="item.baseType.length > 5 ? item.baseType.substring(0, 4) + '...' : item.baseType"
                                size="mini" :color="baseTypeTags[item.baseType]">
                            </u-tag>
                        </view>
                    </template>
                    <template #label>
                        <view style="margin-top: 4px; font-weight: 400; font-size: 12px;color: #777777;">
                            {{item.detailAddress}}
                        </view>
                    </template>
                    <template #icon>
                        <u-avatar shape="circle" size="60" :src="item.baseLogoUrl"
                            customStyle="margin: -3px 5px -3px 0">
                        </u-avatar>
                    </template>
                </u-cell>
            </view>
            <u-loadmore v-if="baseInfo.length !== 0" fontSize="12" :status="loadmoreStatus" @loadmore="loadMoreBase" />
        </u-skeleton> -->
	</view>
</template>

<script setup>
	import {
		ref,
		getCurrentInstance,
		onMounted,
		inject
	} from "vue";
	import {
		listBaseInfoByType,
		listBaseInfoTopByType,
		listBaseSwiper,
		listBaseTypes,
		searchBaseByKeyword,
	} from '../../api/base';
	import {
		listOffspringCodeByCode,
		listRegionsByPCode
	} from "../../api/region";
	import {
		checkTokenApp
	} from "../../api/user";
	import {
		useUserStore
	} from '../../stores/user_store';
	import search from "../../uni_modules/uview-plus/libs/config/props/search";

	const userStore = useUserStore()
	const swiperList = ref([])
	const searchResult = ref([])
	const baseInfo = ref([])
	const baseInfoCache = ref([])
	const showSearchPopup = ref(false)
	const searchLoding = ref(false)
	const searchKeyword = ref('')
	const skeleton = ref(true)
	const showPicker = ref(false)
	const showBaseTypePicker = ref(false)
	const baseTypeTags = ref({})
	const baseTypes = ref([])
	const uToast = ref([])
	const current = ref(0)
	const loadmoreStatus = ref('loadmore')
	const regions = ref([])
	const currentRegion = ref('区域')
	const currentBaseType = ref(-1)
	const baseTypeFilter = ref('全部类型')
	const currentRegionCode = ref(0)
	const uPicker = ref(null)
	const timer = ref(null)
	const innerTimer = ref(null)
	const searchTimer = ref(null)

	const testValue = ref(null)

	const filterDataTime = (mode, options) => {
		console.log(mode);
		console.log(options);

		if (mode === 'year') {
			return options.filter((option) => option % 2 === 0);
		}

		return options;
	}


	listRegionsByPCode(420000).then(res => {
		regions.value = res.data
		currentRegion.value = res.data[0]['regionName']
		currentRegionCode.value = res.data[0]['code']
	})

	uni.$on('clean-user-info', () => {
		userStore.updateMobile('')
		userStore.updateOpenid('')
		userStore.updateToken('')
		userStore.updateUserInfo({})
	})


	onMounted(() => {
		const {
			proxy,
			ctx
		} = getCurrentInstance()
		uPicker.value = ctx.$refs.uPicker

		// checkTokenApp().then((res) => {
		//     if (res.code !== 2000) {
		//         uni.showToast({
		//             icon: "error",
		//             title: '用户未登录'
		//         })
		//         userStore.updateMobile('')
		//         userStore.updateOpenid('')
		//         userStore.updateToken('')
		//         userStore.updateUserInfo({})
		//     }
		// })
	})

	listBaseTypes().then(res => {
		let color = ['#FFD700', '#00CDCD', '#00FF7F', '#D2B48C', '#00CD00', '#777777']
		res.data.forEach((item, index) => {
			baseTypeTags.value[item] = color[index]
			baseTypes.value.push({
				value: item,
				text: item
			})
		})
		baseTypeTags.value['全部类型'] = '#3c9cff'
	})

	listBaseSwiper().then(res => {
		setTimeout(() => {
			swiperList.value = res.data
		}, 500)
	})

	listBaseInfoTopByType(currentBaseType.value, 420000).then(res => {
		if (res.data.length === 0) {
			let timer0 = setInterval(() => {
				if (regions.value.length !== 0) {
					loadMoreBase()
					clearInterval(timer0)
				}
			}, 300)
		} else {
			setTimeout(() => {
				skeleton.value = false
			}, 500)

			baseInfo.value = res.data
			baseInfoCache.value = res.data
		}

	})

	const clickSwiper = (e) => {
		uni.navigateTo({
			url: `/pages/base-detail/base-detail?id=${swiperList.value[e].id}`
		})
	}

	const handleSearch = () => {
		if (searchKeyword.value !== '') {
			searchLoding.value = true
			clearTimeout(searchTimer.value)
			searchTimer.value = setTimeout(() => {
				searchBaseByKeyword(searchKeyword.value, currentRegionCode.value).then(res => {
					searchResult.value = res.data
					searchLoding.value = false
				})
			}, 800)
		}
	}

	const openSearchPopup = () => {
		showSearchPopup.value = true
	}

	const toBaseDetail = (id) => {
		uni.navigateTo({
			url: `/pages/base-detail/base-detail?id=${id}`
		})
	}

	const showDetailBaseType = (type) => {
		uni.showToast({
			icon: "none",
			title: type
		})
	}

	const loadMoreBase = () => {
		current.value += 1
		loadmoreStatus.value = 'loading'

		currentRegion.value = regions.value[0]['regionName']
		currentRegionCode.value = regions.value[0]['code']

		uni.showToast({
			duration: 1000,
			icon: "none",
			title: "加载中...."
		})
		listBaseInfoByType(currentBaseType.value, current.value, 10).then(res => {
			setTimeout(() => {
				skeleton.value = false
				if (res.count !== 0) {
					loadmoreStatus.value = 'loadmore'
				} else {
					loadmoreStatus.value = 'nomore'
				}
			}, 500)
			if (res.count !== 0) {
				baseInfo.value = baseInfo.value.concat(res.data)
				baseInfoCache.value = baseInfo.value
			}
		})
	}

	const chooseRegion = () => {
		showPicker.value = true
		uPicker.value.show()
	}

	const onchange = (e) => {
		console.log(e);
	}

	const onnodeclick = (e) => {
		console.log(e);
	}

	const popClosedHandle = (e) => {
		showPicker.value = false
	}

	const nodeClickHandle = (e) => {
		skeleton.value = true
		currentRegion.value = e['regionName']
		currentRegionCode.value = e['code']
		loadmoreStatus.value = 'loadmore'
		clearTimeout(timer.value)
		timer.value = setTimeout(async () => {
			let filterResult = []
			let {
				data
			} = await listOffspringCodeByCode(e['code'], true)

			for (var i = 0; i < baseInfoCache.value.length; i++) {
				if (baseInfoCache.value[i].regionCode === e['code']) {
					filterResult.push(baseInfoCache.value[i])
					continue
				}
				if (data !== undefined && data[baseInfoCache.value[i].regionCode] !== undefined) {
					filterResult.push(baseInfoCache.value[i])
				}
			}
			baseInfo.value = filterResult
			clearTimeout(innerTimer.value)
			innerTimer.value = setTimeout(() => {
				skeleton.value = false
			}, 200)
		}, 1000)

	}
	const filterBaseType = (e) => {
		skeleton.value = true
		loadmoreStatus.value = 'loadmore'
		currentBaseType.value = baseTypes.value.findIndex(item => item.value === e)
		if (currentBaseType.value === -1) {
			current.value = 0
		}
		listBaseInfoTopByType(currentBaseType.value, 420000).then(res => {
			setTimeout(() => {
				baseInfo.value = res.data
				baseInfoCache.value = res.data
				if (res.data.length === 0) {
					loadMoreBase()
				} else {
					skeleton.value = false
				}
			}, 500)
		})
	}
</script>

<style>
</style>
