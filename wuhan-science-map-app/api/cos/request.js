import instance from "../ajax";
import {
    TENCENT_COS_BUCKET,
    TENCENT_COS_REGION,
    TENCENT_COS_TMP_TOKEN_URL
} from "./config";

const COS = require('../../static/cos-wx-sdk-v5.min.js');

const DIR = 'wuhan-science-app-upload'

export const getAuthorization = (options, callback) => {
    uni.request({
        url: TENCENT_COS_TMP_TOKEN_URL,
        data: {
            duration: 1800,
            prefix: "wuhan-science-app-upload/*"
        },
        success(res) {
            let data = res.data.data 
            callback({
                TmpSecretId: data['temporarySecretId'],
                TmpSecretKey: data['temporarySecretKey'],
                SecurityToken: data['sessionToken'],
                StartTime: data['startTime'],
                ExpiredTime: data['expiredTime'],
            });
        },
        fail(err) {
            console.log(err);
        }
    })
}

const cos = new COS({
        getAuthorization: getAuthorization
    })


export const uploadFile = (tempFile) => {
    let file = tempFile
    let suffix = file.url.split(".")
    let fileName = suffix[0].split('/')
    fileName = fileName[fileName.length - 1]
    suffix = suffix[suffix.length - 1]
    return new Promise((resolve, reject) => {
        cos.uploadFile({
            Bucket: TENCENT_COS_BUCKET,
            Region: TENCENT_COS_REGION,
            Key: `${DIR}/${Date.now().toString(32)}_${fileName}.${suffix}`,
            FilePath: file.url
        }, (err, data) => {
            if (err) {
                reject(err)
            }
            if (data) {
                resolve(data)
            }
        })
        
    })
}

// TODO
export const generateAccessUrl = (key) => instance.get(`/tencent-cos/access?duration=1800&key=${key}`)