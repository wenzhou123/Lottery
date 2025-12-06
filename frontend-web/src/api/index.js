import axios from 'axios'

const javaApi = axios.create({
    baseURL: 'http://localhost:8081/api'
})

const aiApi = axios.create({
    baseURL: 'http://localhost:8000'
})

export const getLatestDraw = (code) => javaApi.get(`/draw/latest?code=${code}`)
export const getDrawHistory = (code) => javaApi.get(`/draw/history?code=${code}`)
export const getDrawByIssue = (code, issueNumber) => javaApi.get(`/draw/issue?code=${code}&issueNumber=${issueNumber}`)
export const getNews = (category) => javaApi.get(`/news/list${category ? `?category=${category}` : ''}`)
export const getAiPrediction = (code) => aiApi.post('/predict', { lottery_code: code })
