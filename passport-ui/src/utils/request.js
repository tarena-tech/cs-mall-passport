import axios from 'axios'

const instance = axios.create({
    baseURL:'http://localhost:8080/user', // url = base url + request url
    // withCredentials: true, // send cookies when cross-domain requests
    headers: {
        'Content-Type': 'application/json'
    },
    timeout: 60000,
    nonConcurrent: true
})

export default instance
