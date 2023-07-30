import { DELETE, GET, POST, PUT } from 'boot/axios'

export const  listAllRegions = () => GET(`/region/listRegionAndChildren/${0}`)
export const  getRegionCentroid = (code: String) => GET(`/region/centroid/${code}`)

