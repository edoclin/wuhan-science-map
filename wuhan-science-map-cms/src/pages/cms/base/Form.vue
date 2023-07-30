<template>
  <el-form :rules="rules" :model="form" label-width="120px" ref="formRef">
    <el-form-item label="基地名称" prop="baseName">
      <el-input v-model="form['baseName']"/>
    </el-form-item>
    <el-form-item label="所属区域" prop="regionCode">
      <el-cascader v-model="form['regionCode']" :options="regions" @change="handleChangeRegion"
                   :props="{label:'regionName', value: 'code'}">
        <template #default="{ node, data }">
          <span>{{ data['regionName'] }}</span>
          <span v-if="!node.isLeaf"> ({{ data['children'].length }}) </span>
        </template>
      </el-cascader>
    </el-form-item>

    <el-form-item label="详细地址" prop="detailAddress">
      <el-input v-model="form['detailAddress']"/>
    </el-form-item>
    <el-form-item label="基地类型" prop="baseType">
      <el-select v-model="form['baseType']" placeholder="请选择基地类型" style="width: 100%">
        <el-option v-for="type in baseTypes" :key="type" :label="type"
                   :value="type"/>
      </el-select>
    </el-form-item>
    <el-form-item label="基地Logo" prop="baseLogo">
      <el-upload :on-preview="handlePreviewUpload" list-type="picture-card" v-model:file-list="fileList"
                 :auto-upload="false" accept="image/*"
                 :on-change="handleChangeFileUpload">
        <el-button type="primary" :loading="uploading">
          {{ uploading ? '上传中...' : (fileList.length === 1 ? '重新上传' : '点击上传') }}
        </el-button>
        <template #tip>
          <div class="el-upload__tip">
            <el-progress
                style="width: 202px"
                v-if="uploading"
                :percentage="100"
                :indeterminate="true"
                :duration="1"
                :format="() => ''"
            />
            <span v-else>请选择图片文件上传</span>
          </div>
        </template>
      </el-upload>
    </el-form-item>
    <el-form-item label="友情链接" prop="linkIds">
      <el-select multiple v-model="form['linkIds']" placeholder="请选择友情链接" style="width: 100%">
        <el-button :icon="Edit" text @click="addNewLink">新增链接</el-button>
        <el-option v-for="link in links" :key="link.id" :label="link.linkName"
                   :value="link.id"/>
      </el-select>
    </el-form-item>
    <el-form-item label="置顶展示">
      <el-radio-group v-model="form['top']">
        <el-radio-button :label="true">是</el-radio-button>
        <el-radio-button :label="false">否</el-radio-button>
      </el-radio-group>
    </el-form-item>
    <el-form-item label="轮播图展示">
      <el-radio-group v-model="form['carousel']">
        <el-radio-button :label="true">是</el-radio-button>
        <el-radio-button :label="false">否</el-radio-button>
      </el-radio-group>
    </el-form-item>
    <el-form-item label="轮播展示图片" prop="carouselImageAccessKey">
      <el-upload :on-preview="handlePreviewUpload" list-type="picture-card" v-model:file-list="fileList4Carousel"
                 :auto-upload="false" accept="image/*"
                 :on-change="handleChangeFileUpload4Carousel">
        <el-button type="primary" :loading="uploading4Carousel">
          {{ uploading4Carousel ? '上传中...' : (fileList4Carousel.length === 1 ? '重新上传' : '点击上传') }}
        </el-button>
        <template #tip>
          <div class="el-upload__tip">
            <el-progress
                style="width: 202px"
                v-if="uploading4Carousel"
                :percentage="100"
                :indeterminate="true"
                :duration="1"
                :format="() => ''"
            />
            <span v-else>请选择图片文件上传</span>
          </div>
        </template>
      </el-upload>
    </el-form-item>
    <el-form-item label="基地区域" prop="polygonGeometry">
      <div style="height: calc(100vh - 80px);width: 100%;">
        <el-amap :center="amapParam.center" :zoom="amapParam.zoom">
          <el-amap-search-box :visible="true" @select="selectPoi" @choose="choosePoi"/>
          <el-amap-control-map-type :visible="true"></el-amap-control-map-type>
          <el-amap-control-geolocation :visible="true" @complete="getLocation"/>
          <el-amap-mouse-tool v-if="form.polygonGeometry.length === 0" type="polygon" :auto-clear="true"
                              @draw="drawMouseTool"/>
          <el-amap-polygon :path="form.polygonGeometry" :visible="true" :editable="amapPolygon.editable"
                           :draggable="amapPolygon.editable" @end="end"/>
        </el-amap>
        <el-form-item v-if="form.polygonGeometry.length !== 0" label="操作" style="margin-top: 10px" label-width="50">
          <el-radio-group v-model="amapPolygon.editable">
            <el-radio-button :label="true">编辑</el-radio-button>
            <el-radio-button :label="false">锁定</el-radio-button>
          </el-radio-group>
        </el-form-item>

      </div>
    </el-form-item>
    <el-form-item label="基地介绍" prop="descRichText" style="margin-top: 60px">
      <div style="border: 1px solid #ccc">
        <Toolbar style="border-bottom: 1px solid #ccc" :editor="editorRef4Desc" :defaultConfig="toolbarConfig4Desc"
                 mode="default"/>
        <Editor style="height:  calc(100vh - 120px);width: 100%; overflow-y: hidden;" v-model="valueHtml4Desc"
                :defaultConfig="editorConfig4Desc" mode="default" @onCreated="handleCreated4Desc"/>
      </div>
    </el-form-item>
    <el-form-item label="参观指南" prop="visitTutorial">
      <div style="border: 1px solid #ccc">
        <Toolbar style="border-bottom: 1px solid #ccc" :editor="editorRef4Tutorial"
                 :defaultConfig="toolbarConfig4Tutorial"
                 mode="default"/>
        <Editor style="height:  calc(100vh - 120px);width: 100%; overflow-y: hidden;" v-model="valueHtml4Tutorial"
                :defaultConfig="editorConfig4Tutorial" mode="default" @onCreated="handleCreated4Tutorial"/>
      </div>
    </el-form-item>
    <el-form-item label="当前状态">
      <el-select v-model="form['status']" placeholder="请选择状态" style="width: 100%">
        <el-option v-for="status in status" :key="status.key" :label="status.value"
                   :value="status.key"/>
      </el-select>
    </el-form-item>
    <el-form-item>
      <el-button type="primary" @click="onSubmit(formRef)">{{ data ? '更新' : '创建' }}</el-button>
      <el-button v-if="!data" @click="onResetForm(formRef)">重置</el-button>
    </el-form-item>
  </el-form>
</template>
<script setup>
import '@wangeditor/editor/dist/css/style.css'
import { Editor, Toolbar } from '@wangeditor/editor-for-vue'
import { postScienceBase, putScienceBase } from 'src/api/science-base'
import { ElMessage } from 'element-plus'
import { Edit } from '@element-plus/icons-vue'
import { useMapState } from 'src/stores'
import { useCommonStore } from 'src/stores/common_store'
import { getRegionCentroid, listAllRegions } from 'src/api/region'
import { regeo } from 'src/api/amap'
import { listBaseTypes } from 'src/api/common'
import { openURL } from 'quasar'
import { getAccessUrl, sliceUploadFile } from 'src/api/cos'
import { getLinkSelector } from 'src/api/link'
import LinkFormVue from 'pages/cms/link/Form.vue'

const { status } = useMapState(useCommonStore, ['status'])

const bus = inject('bus')

const editorRef4Desc = shallowRef()
const editorRef4Tutorial = shallowRef()
const valueHtml4Desc = ref('')
const valueHtml4Tutorial = ref('')

const addNewLink = () => {
  bus.emit('edit-item', {
    record: {},
    component: LinkFormVue,
    title: '新增链接',
    name: Math.random()
  })
}

const links = ref([])

bus.on('update-base-link-selector', () => {
  getLinkSelector().then(res => {
    links.value = res.data
  })
})

getLinkSelector().then(res => {
  links.value = res.data
})

onBeforeUnmount(() => {
  let editor = editorRef4Desc.value
  if (editor != null) editor.destroy()

  editor = editorRef4Tutorial.value
  if (editor != null) editor.destroy()

})

const handleCreated4Desc = (editor) => {
  editorRef4Desc.value = editor
}

const handleCreated4Tutorial = (editor) => {
  editorRef4Tutorial.value = editor
}

const toolbarConfig4Desc = reactive({
  excludeKeys: [
    'fullScreen'
  ]
})

const toolbarConfig4Tutorial = reactive({
  excludeKeys: [
    'fullScreen'
  ]
})
const editorConfig4Desc = reactive({
  placeholder: '请输入基地介绍',
  MENU_CONF: {
    'uploadImage': {
      maxFileSize: 1024 * 1024 * 10,
      maxNumberOfFiles: 100,
      allowedFileTypes: ['image/*'],
      metaWithUrl: false,
      withCredentials: true,
      timeout: 10 * 1000,
      async customUpload (file, insertFn) {
        sliceUploadFile(file, 'wuhan-science-rich-text').then(res => {
          insertFn(`https://${res.Location}`, '', `https://${res.Location}`)
        }).catch(err => {
          ElMessage({
            type: 'error',
            message: '服务器繁忙,请重试!'
          })
        })
      }
    },
    'uploadVideo': {
      maxFileSize: 1024 * 1024 * 100 * 10,
      maxNumberOfFiles: 10,
      allowedFileTypes: ['video/*', 'audio/*'],
      metaWithUrl: false,
      withCredentials: true,
      timeout: 20 * 1000,
      async customUpload (file, insertFn) {
        sliceUploadFile(file, 'wuhan-science-rich-text').then(res => {
          insertFn(`https://${res.Location}`, '', `https://${res.Location}`)
        }).catch(err => {
          ElMessage({
            type: 'error',
            message: '服务器繁忙,请重试!'
          })
        })
      }
    }
  }
})

const editorConfig4Tutorial = reactive({
  placeholder: '请输入参观指南',
  MENU_CONF: {
    'uploadImage': {
      maxFileSize: 1024 * 1024 * 10,
      maxNumberOfFiles: 100,
      allowedFileTypes: ['image/*'],
      metaWithUrl: false,
      withCredentials: true,
      timeout: 10 * 1000,
      async customUpload (file, insertFn) {
        sliceUploadFile(file, 'wuhan-science-rich-text').then(res => {
          insertFn(`https://${res.Location}`, '', `https://${res.Location}`)
        }).catch(err => {
          ElMessage({
            type: 'error',
            message: '服务器繁忙,请重试!'
          })
        })
      }
    },
    'uploadVideo': {
      maxFileSize: 1024 * 1024 * 100 * 10,
      maxNumberOfFiles: 10,
      allowedFileTypes: ['video/*', 'audio/*'],
      metaWithUrl: false,
      withCredentials: true,
      timeout: 20 * 1000,
      async customUpload (file, insertFn) {
        sliceUploadFile(file, 'wuhan-science-rich-text').then(res => {
          insertFn(`https://${res.Location}`, '', `https://${res.Location}`)
        }).catch(err => {
          ElMessage({
            type: 'error',
            message: '服务器繁忙,请重试!'
          })
        })
      }
    }
  }
})

const uploading = ref(false)
const fileList = ref([])

const handlePreviewUpload = (e) => {
  openURL(e.url)
}

const handleChangeFileUpload = (uploadFile) => {
  uploading.value = true
  fileList.value = []
  sliceUploadFile(uploadFile.raw, 'base-logo').then(data => {
    form['baseLogo'] = data['Key']
    ElMessage({
      type: 'success',
      message: '上传成功'
    })
    uploading.value = false
    getAccessUrl(data['Key']).then(res => {
      fileList.value.push({
        name: '',
        url: res.data
      })
    })
  }).catch(err => {
    uploading.value = false
    ElMessage({
      type: 'error',
      message: '服务器繁忙,请重试!'
    })
  })
}

const uploading4Carousel = ref(false)
const fileList4Carousel = ref([])
const handleChangeFileUpload4Carousel = (uploadFile) => {
  uploading4Carousel.value = true
  fileList4Carousel.value = []
  sliceUploadFile(uploadFile.raw, 'base-carousel').then(data => {
    form['carouselImageAccessKey'] = data['Key']
    ElMessage({
      type: 'success',
      message: '上传成功'
    })
    uploading4Carousel.value = false
    getAccessUrl(data['Key']).then(res => {
      fileList4Carousel.value.push({
        name: '',
        url: res.data
      })
    })
  }).catch(err => {
    uploading4Carousel.value = false
    ElMessage({
      type: 'error',
      message: '服务器繁忙,请重试!'
    })
  })
}

const formRef = ref()
const regions = ref([])

const amapParam = reactive({
  center: [114.34831510957784, 30.621785735049563],
  zoom: 10
})

const selectPoi = (e) => {
  amapParam.center = [e.poi.location.lng, e.poi.location.lat]
  regeo(e.poi.location.lng, e.poi.location.lat).then(data => {
    form.baseLocationText = data
  })
}
const choosePoi = (e) => {
}

const getLocation = (e) => {
  amapParam.center = [e.poi.location.lng, e.poi.location.lat]
}

const drawMouseTool = (e) => {
  form.polygonGeometry = e
}

const amapPolygon = reactive({
  editable: true
})
const end = ({
  type,
  target
}) => {
  form.polygonGeometry = target._opts.path
}

listAllRegions().then(res => {
  regions.value = res.data
})

const baseTypes = ref([])
listBaseTypes().then(res => {
  baseTypes.value = res.data
})

const handleChangeRegion = (e) => {
  form['regionCode'] = e[e.length - 1]
  getRegionCentroid(form['regionCode']).then(res => {
    amapParam.center = [res['data']['lng'], res['data']['lat']]
    regeo(res['data']['lng'], res['data']['lat']).then(resp => {
      form['detailAddress'] = resp
    })
  })
}

const form = reactive({
  regionCode: 0,
  baseName: '',
  detailAddress: '',
  baseLogo: '',
  baseType: '',
  descRichText: '',
  visitTutorial: '',
  linkIds: '',
  polygonGeometry: [],
  top: false,
  carousel: false,
  displayIndex: 0
})

const rules = reactive({
  'baseName': [
    {
      required: true,
      trigger: 'blur',
      message: '请输入基地名称'
    },
  ],
  'regionCode': [
    {
      required: true,
      trigger: 'blur',
      message: '请输入基地所属区域'
    },
  ],
  'detailAddress': [
    {
      required: true,
      trigger: 'blur',
      message: '请输入基地详细地址'
    },
  ],
  'baseLogo': [
    {
      required: true,
      trigger: 'blur',
      message: '请上传基地Logo'
    },
  ],
  'baseType': [
    {
      required: true,
      trigger: 'blur',
      message: '请选择基地类型'
    },
  ],
  'descRichText': [
    {
      required: true,
      trigger: 'blur',
      message: '请输入基地介绍'
    },
  ],
  'visitTutorial': [
    {
      required: true,
      trigger: 'blur',
      message: '请输入参观指南'
    },
  ],
})
const onResetForm = (formEl) => {
  if (formEl) {
    formEl.resetFields()
  }
}

const onSubmit = (formEl) => {
  form['descRichText'] = valueHtml4Desc.value
  form['visitTutorial'] = valueHtml4Tutorial.value

  if (amapPolygon.editable) {
    ElMessage({
      type: 'error',
      message: '请锁定地图编辑后提交'
    })
    return
  }
  const len = form['regionCode'].length
  if (len !== 1 && typeof form['regionCode'] !== 'number') {
    console.log(len)
    form['regionCode'] = form['regionCode'][len - 1]
  }

  formEl.validate(async (valid) => {
    if (valid) {
      if (props.data && JSON.stringify(props.data) !== '{}') {
        await putScienceBase(form).then(res => {
          ElMessage({
            type: 'success',
            message: res.data,
          })
        })
      } else {
        await postScienceBase(form).then(res => {
          ElMessage({
            type: 'success',
            message: res.data,
          })
          onResetForm(formEl)
        })
      }
      fileList.value = []
      valueHtml4Desc.value = ''
      valueHtml4Tutorial.value = ''
      bus.emit('update-base-table', fileList)
      bus.emit('update-base-selector')
    }
  })
}

const props = defineProps({
  data: {}
})
if (props.data && JSON.stringify(props.data) !== '{}') {
  form['id'] = props.data['id']
  form['baseName'] = props.data['baseName']
  form['detailAddress'] = props.data['detailAddress']
  form['baseLogo'] = props.data['baseLogo']
  form['status'] = status.value.find(item => item.value === props.data['status']).key
  form['polygonGeometry'] = props.data['polygonGeometry']

  form['descRichText'] = props.data['descRichText']
  form['regionCode'] = props.data['regionCodes']
  form['baseType'] = props.data['baseType']

  valueHtml4Desc.value = props.data['descRichText']

  form['visitTutorial'] = props.data['visitTutorial']
  form['linkIds'] = props.data['linkIds']
  form['top'] = props.data['top']
  form['carousel'] = props.data['carousel']
  form['carouselImageAccessKey'] = props.data['carouselImageAccessKey']
  valueHtml4Tutorial.value = props.data['visitTutorial']

  fileList.value = [{
    name: '',
    url: props.data['baseLogoUrl']
  }]
  fileList4Carousel.value = [{
    name: '',
    url: props.data['carouselImageAccessKeyUrl']
  }]
  amapParam.center = [props.data['centroid'].lng, props.data['centroid'].lat]
}

</script>
