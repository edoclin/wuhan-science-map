<template>
  <div>
    <draggable-table :table-data="tableData" :display-rows="displayRows"
                     @onDraggableChange="handleDraggable" @submitChange="submitChangeIndex"></draggable-table>
  </div>
</template>

<script setup>

import DraggableTable from 'components/DraggableTable.vue'
import { ElMessage, ElMessageBox } from 'element-plus'
import { listNewsDisplayIndex, putNewsDisplayIndex } from 'src/api/news'

onMounted(() => {
  ElMessageBox.alert('该功能可改变小程序端置顶热点资讯的显示顺序, 长按数据项拖动即可改变顺序, 表格中的顺序即为小程序端的展示顺序', '热点资讯排序提示', {
    confirmButtonText: '我知道了',
    callback: (action) => {
    },
  })
})
const tableData = ref([])
const queryParam = reactive({
  isAsc: false,
  orderColumns: ['createdTime'],
  conditions: [
    {
      'column': 'isTop',
      'value': true,
      'isAnd': true
    }
  ]
})
const bus = inject('bus')

bus.on('update-goods-list', () => {
  listNewsDisplayIndex().then(res => {
    tableData.value = res.data
  })
})
listNewsDisplayIndex().then(res => {
  tableData.value = res.data
})

const displayRows = ref([
  {
    column: 'newsTitle',
    label: '新闻标题'
  },
  {
    column: 'newsType',
    label: '新闻类型'
  },
  {
    column: 'creator',
    label: '撰稿人'
  },
  {
    column: 'createdTime',
    label: '发布时间'
  },

])

const handleDraggable = (e) => {
  ElMessage.warning({
    message: '改变顺序后请点击"提交更改"后生效'
  })
}

const submitChangeIndex = (e) => {
  let batch = ref([])
  tableData.value.forEach((item, index) => {
    batch.value.push({
      id: item.id,
      displayIndex: index + 1
    })
  })
  putNewsDisplayIndex(batch.value).then(res => {
    ElMessage.success(res.data)
  })
}
</script>
<style scoped>
</style>
