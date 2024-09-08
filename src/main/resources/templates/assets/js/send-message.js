import axios from 'axios'
export function sendPhoneAuthCode(data) {
  return axios.post('/api/third/message/phone-code', data)
}
export function sendEmailAuthCode(data) {
  return axios.post('/api/third/message/email-code', data)
}