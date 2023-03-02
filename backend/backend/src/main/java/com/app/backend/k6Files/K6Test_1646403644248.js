import http from 'k6/http'
import { sleep } from 'k6'

export const options = {
	stages: [
		{ duration: '4', target: 2},
		{ duration: '6', target: 3},
		{ duration: '3', target: 6},
 	],
};

export default function () {
 const BASE_URL = 'http://localhost:8080/api/getPlanDetails'

	const responses = http.batch([
		['GET', '${BASE_URL}', null, {tags: { name: 'localtest' } }]
	]);

	sleep(1);

}