function fetchPrice() {
    fetch('http://localhost:8000/price')
        .then(response => response.text()) // 서버 응답을 텍스트로 변환
        .then(data => {
            console.log(data); // 콘솔에 데이터 출력
        })
        .catch(error => console.error('Error:', error)); // 에러 처리
}
// 1초마다 가격 정보 업데이트
setInterval(fetchPrice, 1000);

// 초기 가격 로드
fetchPrice();
