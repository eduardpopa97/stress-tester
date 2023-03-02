import http from 'k6/http'
import { sleep } from 'k6'

export const options = {
	stages: [
		{ duration: '1', target: 2}
		{ duration: '1', target: 1}
 	],
};

export default function () {
 const BASE_URL = 'facebook.com'

	const responses = http.batch([
		['GET', '${BASE_URL}', null, {tags: { name: 'localtest' } }]
	]);

	sleep(1);

}