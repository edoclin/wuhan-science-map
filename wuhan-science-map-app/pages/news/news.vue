<template>
    <view>
        <u-popup :show="showSearchPopup" mode="top" @close="() => showSearchPopup = false">
            <u-search :clearabled="true" placeholder="请输入标题关键字搜索" :showAction="false" v-model="searchKeyword"
                @change="handleSearch">
            </u-search>
            <u-loading-icon :show="searchLoding"></u-loading-icon>
            <u-divider v-if="searchKeyword === ''" text="请输入关键字搜索"></u-divider>
            <u-empty v-else-if="searchResult.length === 0 && !searchLoding" mode="search"
                icon="http://cdn.uviewui.com/uview/empty/search.png" />
            <view v-if="searchResult.length !== 0 && !searchLoding">
                <u-cell v-for="(item, index) in searchResult" :key="index" :label="item.updatedTime"
                    @click="toNewsDetail(item.id)">
                    <template #title>
                        {{item.newsTitle}}
                    </template>
                    <template #value>
                        <view style="width: 20vw;">
                            <u-tag plain color="#3c9cff" borderColor="#3c9cff" :text="item.newsType" size="mini"
                                v-if="item.newsType ==='科普动态' && subsectionCurrent === 0">
                            </u-tag>
                            <u-tag plain color="#5ac725" borderColor="#5ac725" :text="item.newsType" size="mini"
                                v-if="item.newsType ==='科技新闻' && subsectionCurrent === 0">
                            </u-tag>
                        </view>
                    </template>
                    <template #icon>
                        <u-avatar shape="square" size="60" :src="item.coverAccessKeyUrl"
                            customStyle="margin: -3px 5px -3px 0">
                        </u-avatar>
                    </template>
                </u-cell>
                <u-divider text="已全部加载"></u-divider>
            </view>
        </u-popup>
        <u-sticky>
            <u-search shape="square" @focus="openSearchPopup" :clearabled="true" placeholder="请输入标题关键字搜索"
                :showAction="false">
            </u-search>
            <u-swiper height="28vh" indicator indicatorMode="line" :list="swiperList" @change="changeSwiper"
                @click="clickSwiper" keyName="coverAccessKeyUrl" showTitle>
            </u-swiper>
            <u-subsection :list="subsectionList" :current="subsectionCurrent" @change="changeCurrentSubsection">
            </u-subsection>
        </u-sticky>
        <u-skeleton :loading="skeleton" :animate="true" :rows="40" :title="false">
            <u-empty v-if="newsInfo.length === 0" mode="news" icon="http://cdn.uviewui.com/uview/empty/news.png"
                text="来早了...">
            </u-empty>
            <view  v-else>
                <u-cell :isLink="subsectionCurrent !== 0" v-for="(item, index) in newsInfo" :key="index"
                    :label="item.updatedTime" @click="toNewsDetail(item.id)">
                    <template #title>
                        {{item.newsTitle}}
                    </template>
                    <template #value>
                        <view style="width: 20vw;">
                            <u-tag plain color="#3c9cff" borderColor="#3c9cff" :text="item.newsType" size="mini"
                                v-if="item.newsType ==='科普动态' && subsectionCurrent === 0">
                            </u-tag>
                            <u-tag plain color="#5ac725" borderColor="#5ac725" :text="item.newsType" size="mini"
                                v-if="item.newsType ==='科技新闻' && subsectionCurrent === 0">
                            </u-tag>
                        </view>
                    </template>
                    <template #icon>
                        <u-avatar shape="square" size="60" :src="item.coverAccessKeyUrl"
                            customStyle="margin: -3px 5px -3px 0">
                        </u-avatar>
                    </template>
                </u-cell>
                <u-loadmore fontSize="12" :status="loadmoreStatus" @loadmore="loadMoreNews" />
            </view>
            
        </u-skeleton>
    </view>
</template>

<script setup>
    import {
        ref
    } from "vue";
    import {
        listNewsInfoByType,
        listNewsInfoTopByType,
        listNewsSwiper,
        listNewsTypes,
        searchNewsByKeyword
    } from "../../api/news";
    import {
        onPullDownRefresh
    } from "@dcloudio/uni-app"

    onPullDownRefresh(() => {
        skeleton.value = true
        current.value = 0
        subsectionCurrent.value = 0
        loadmoreStatus.value = 'loadmore'
        listNewsInfoTopByType(-1).then(res => {
            setTimeout(() => {
                newsInfo.value = res.data
                skeleton.value = false
                uni.stopPullDownRefresh()
            }, 500)
        })
    })


    const swiperList = ref([])
    const newsInfo = ref([])
    const skeleton = ref(true)
    const subsectionList = ref([])
    const searchKeyword = ref('')
    const searchResult = ref('')
    const current = ref(0)
    const loadmoreStatus = ref('loadmore')
    const showSearchPopup = ref(false)
    const searchLoding = ref(false)
    const searchTimer = ref(null)

    listNewsTypes().then(res => {
        subsectionList.value = [...res.data]
    })
    const subsectionCurrent = ref(0)

    listNewsInfoTopByType(-1).then(res => {
        setTimeout(() => {
            newsInfo.value = res.data
            skeleton.value = false
        }, 500)

    })

    listNewsSwiper().then(res => {
        setTimeout(() => swiperList.value = res.data, 500)
    })

    const loadMoreNews = () => {
        current.value += 1
        loadmoreStatus.value = 'loading'
        listNewsInfoByType(subsectionCurrent.value - 1, current.value, 10).then(res => {
            setTimeout(() => {
                if (res.count !== 0) {
                    newsInfo.value = newsInfo.value.concat(res.data)
                    loadmoreStatus.value = 'loadmore'
                } else {
                    loadmoreStatus.value = 'nomore'
                }
            }, 500)
        })
    }

    const changeSwiper = (e) => {}

    const clickSwiper = (e) => {
        toNewsDetail(swiperList.value[e].id)
    }

    const changeCurrentSubsection = (e) => {
        current.value = 0
        subsectionCurrent.value = e
        loadmoreStatus.value = 'loadmore'
        skeleton.value = true
        listNewsInfoTopByType(e - 1).then(res => {
            setTimeout(() => {
                newsInfo.value = res.data
                skeleton.value = false
                console.log(skeleton.value);
            }, 500)

        })
    }
    const toNewsDetail = (id) => {
        uni.navigateTo({
            url: `/pages/news-detail/news-detail?id=${id}`
        })
    }

    const openSearchPopup = () => {
        showSearchPopup.value = true
    }

    const handleSearch = () => {
        if (searchKeyword.value !== '') {
            searchLoding.value = true
            clearTimeout(searchTimer.value)
            searchTimer.value = setTimeout(() => {
                searchNewsByKeyword(searchKeyword.value).then(res => {
                    searchResult.value = res.data
                    searchLoding.value = false
                })
            }, 800)
        }
    }
</script>

<style>

</style>
