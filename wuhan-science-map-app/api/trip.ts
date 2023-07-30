import instance from "./ajax";

// 获取置顶的智慧行/全部智慧行 -> tripName accessKey descSimple startingTime
export const zhihuiTrip = (top:boolean) =>instance.get(`/zhihuiTrip/app/trip/${top}`)
// 根据id获取的智慧行 -> itemName accessKey descSimple
export const zhihuiTripListById = (id: string) => instance.get(`/zhihuiTripItem/app/tripList/${id}`)
// 根据子路线的id获得子路线的详细信息 -> itemName accessKey descSimple descRichText courseId
export const tripItemDetailById = (id: string) => instance.get(`/zhihuiTripItem/app/detailItem/${id}`)
// 检查当前活动是否还有名额
export const checkItemStockById = (id: string) => instance.get(`/zhihuiTripItem/app/${id}`)

export const registerActive = (form:{}) => instance.post(``,form)